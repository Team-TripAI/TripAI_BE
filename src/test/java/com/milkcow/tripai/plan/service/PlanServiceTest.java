package com.milkcow.tripai.plan.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.plan.domain.Plan;
import com.milkcow.tripai.plan.dto.PlanRequestDto;
import com.milkcow.tripai.plan.dto.PlanResponseDto;
import com.milkcow.tripai.plan.dto.accommodation.AccommodationPlanDto;
import com.milkcow.tripai.plan.dto.attraction.AttractionPlanDto;
import com.milkcow.tripai.plan.dto.flight.FlightPlanDto;
import com.milkcow.tripai.plan.dto.restaurant.RestaurantPlanDto;
import com.milkcow.tripai.plan.embedded.DayOfWeek;
import com.milkcow.tripai.plan.embedded.PlaceHour;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.repository.PlanRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlanServiceTest {
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 일정_등록() throws Exception {
        //given
        Member member = getMember("test@test.com");
        memberRepository.save(member);
        PlanRequestDto planDto = getPlanDto();
        //when
        long id = planService.create(member, planDto);
        //then
        assertThat(id).isPositive();
    }

    @Test
    public void 일정_조회() throws Exception {
        //given
        Member member = getMember("test@test.com");
        long id = createPlan(member);
        //when
        PlanResponseDto planDto = planService.find(member, id);

        //then
        assertThat(planDto.getName()).isEqualTo("도쿄");
    }

    @Test
    public void 일정_조회_예외() throws Exception {
        //given
        Member member1 = getMember("test1@test.com");
        Member member2 = getMember("test2@test.com");
        long id = createPlan(member1);
        //when
        Assertions.assertThatThrownBy(
                        () -> planService.find(member2, id))
                .isInstanceOf(PlanException.class);
    }

    @Test
    public void 일정_삭제() throws Exception {
        //given
        Member member = getMember("test@test.com");
        long id = createPlan(member);
        //when
        planService.delete(member, id);
        //then
        Optional<Plan> planById = planRepository.findPlanById(id);
        assertThat(planById.isEmpty()).isTrue();
    }

    @Test
    public void 일정_삭제_예외() throws Exception {
        //given
        Member member1 = getMember("test1@test.com");
        Member member2 = getMember("test2@test.com");
        long id = createPlan(member1);
        //when
        Assertions.assertThatThrownBy(
                        () -> planService.delete(member2, id))
                .isInstanceOf(PlanException.class);
    }

    private static Member getMember(String email) {
        return Member.builder()
                .email(email)
                .nickname("test")
                .build();
    }

    private static PlanRequestDto getPlanDto() {
        AccommodationPlanDto accommodationPlanDto = new AccommodationPlanDto(
                "Lodging Tokyo Ueno",
                35.7176401814438,
                139.785188392868,
                "https://cf.bstatic.com/xdata/images/hotel/square60/497650805.jpg?k=8a38c4d87eff3bf19478446616bf2349af721648120d685b67097d958b62ae01&o=",
                LocalDate.of(2023, 12, 25),
                LocalDate.of(2023, 12, 31));

        FlightPlanDto flightPlanDto = new FlightPlanDto(
                "YP101",
                "YP",
                "ICN",
                "LAX",
                LocalDateTime.of(2023, 12, 25, 13, 30),
                LocalDateTime.of(2023, 12, 25, 7, 20),
                "https://www.tripadvisor.com/CheapFlightsPartnerHandoff?searchHash=3a43bb45deca656b05417e46cead30e9&provider=SkyScanner|1|36&area=FLTCenterColumn|0|1|ItinList|2|Meta_ItineraryPrice&resultsServlet=CheapFlightsSearchResults&handoffPlatform=desktop&impressionId=&totalPricePerPassenger=873900.06");

        RestaurantPlanDto restaurantPlanDto = new RestaurantPlanDto(
                "Gyopao Gyoza Roppongi",
                35.663578,
                139.73212,
                getPlaceHours(),
                "https://media-cdn.tripadvisor.com/media/photo-l/18/cc/b9/54/japanese-and-taiwan-fusion.jpg"
        );

        AttractionPlanDto attractionPlanDto = new AttractionPlanDto(
                "Meiji Jingu Shrine",
                35.676167,
                139.68852,
                getPlaceHours(),
                "https://media-cdn.tripadvisor.com/media/photo-l/1b/62/93/a0/caption.jpg"
        );

        return PlanRequestDto.builder()
                .name("도쿄")
                .start(LocalDate.of(2023, 12, 25))
                .end(LocalDate.of(2023, 12, 31))
                .total(100)
                .accommodation(30)
                .flight(30)
                .restaurant(30)
                .attraction(10)
                .accommodationList(List.of(accommodationPlanDto))
                .flightList(List.of(flightPlanDto))
                .restaurantList(List.of(restaurantPlanDto))
                .attractionList(List.of(attractionPlanDto))
                .build();
    }

    private static List<PlaceHour> getPlaceHours() {
        PlaceHour sun = new PlaceHour(DayOfWeek.SUN, "12:00", "23:45");
        PlaceHour mon = new PlaceHour(DayOfWeek.MON, "12:00", "23:45");
        PlaceHour tue = new PlaceHour(DayOfWeek.TUE, "12:00", "23:45");
        PlaceHour wed = new PlaceHour(DayOfWeek.WED, "12:00", "23:45");
        PlaceHour thu = new PlaceHour(DayOfWeek.THU, "12:00", "23:45");
        PlaceHour fri = new PlaceHour(DayOfWeek.FRI, "12:00", "23:45");
        PlaceHour sat = new PlaceHour(DayOfWeek.SAT, "12:00", "23:45");

        return List.of(sun, mon, tue, wed, thu, fri, sat);
    }

    private long createPlan(Member member) {
        memberRepository.save(member);
        PlanRequestDto planDto = getPlanDto();
        return planService.create(member, planDto);
    }
}