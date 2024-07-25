# TripAI
AI 기반 여행지 추천 서비스, TripAI • 백엔드 레포지토리

> TripAI는 여행장소 사진을 기반으로 여행지를 추천해주는 서비스입니다!

## 🛠️ 사용 스택
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white"/></a>
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white"/></a>
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=Spring Security&logoColor=white"/></a>
<br />
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/></a>
<br />
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat&logo=Amazon Web Services&logoColor=white"/></a>
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white"/></a>
<br />
<a href="_blank" target="_blank"><img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=Slack&logoColor=white"/></a>

## 📁 Project Structure
```java
└─src
    └─main.java.com.milkcow.tripai
       ├─<도메인>    // 각 도메인 (e.g. article, member, ...)
       │  ├─controller  // 해당 도메인의 컨트롤러
       │  ├─domain      // 엔티티 클래스
       │  ├─dto         // DTO 클래스
       │  ├─exception   // 예외 클래스
       │  ├─repository  // 도메인 리포지토리
       │  ├─result      // 예외 코드 enum 클래스
       │  ├─service     // 도메인 서비스
       │  └─util        // 유틸리티 클래스
       ├─global
       │  ├─config      // Configuration 클래스
       │  ├─dto         // 공통 응답 DTO를 형식화한 클래스
       │  ├─exception   // 전역적으로 발생하는 예외 클래스
       │  └─result      // 전역적인 예외 코드 enum 클래스
       ├─jwt        // JWT Token 관련 (e.g. Token Provider)
       │  └─handler     // JWT 예외 상황에 대한 handler (e.g. AccessDeniedHandler)
       └─security   // Spring Security 관련
           └─filter     // 커스텀 필터
```

## Developers
| 박수영 | 박정환 | 정준서 |
|:------:|:------:|:------:|
|<img src="https://github.com/user-attachments/assets/c9983779-5ee4-40e0-99be-71cfd23ac7a5" width="100">|<img src="https://github.com/user-attachments/assets/15b80111-dcbe-428b-8325-43b077af2a85" width="100">|<img src="https://github.com/user-attachments/assets/c24429ed-c1c5-4e77-971d-5ac168756067" width="100">|
|[clap-0](https://github.com/clap-0)|[Hwanvely](https://github.com/Hwanvely)|[juns1s](https://github.com/juns1s)|
|Backend|Backend|Backend|
