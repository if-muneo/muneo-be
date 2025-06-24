<div align="center">
👥 Team Members
<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=200&section=header&text=Meet%20Our%20Team&fontSize=50&fontColor=fff" />
<table>
<tr>
<th>🏆 팀장</th>
<th>🧠 Vector Database 팀</th>
<th>💬 프롬프트 & 채팅팀</th>
<th>🗄️ RDB 팀</th>
</tr>
<tr>
<td align="center">
<a href="https://github.com/hjg727">
<img src="https://avatars.githubusercontent.com/hjg727?s=100" width="100px;" alt="hjg727"/><br/>
<b>hjg727</b><br/>
홍정기
</a>
</td>
<td align="center">
<a href="https://github.com/minsukim9900">
<img src="https://avatars.githubusercontent.com/minsukim9900?s=80" width="80px;" alt="minsukim9900"/><br/>
<b>minsukim9900</b><br/>
김민수
</a>
<br/><br/>
<a href="https://github.com/marineAqu">
<img src="https://avatars.githubusercontent.com/marineAqu?s=80" width="80px;" alt="marineAqu"/><br/>
<b>marineAqu</b><br/>
김도연
</a>
</td>
<td align="center">
<a href="https://github.com/Jeong-Minkyeong">
<img src="https://avatars.githubusercontent.com/Jeong-Minkyeong?s=80" width="80px;" alt="Jeong-Minkyeong"/><br/>
<b>Jeong-Minkyeong</b><br/>
정민경
</a>
<br/><br/>
<a href="https://github.com/likerhythm">
<img src="https://avatars.githubusercontent.com/likerhythm?s=80" width="80px;" alt="likerhythm"/><br/>
<b>likerhythm</b><br/>
최정민
</a>
</td>
<td align="center">
<a href="https://github.com/sangyunpark99">
<img src="https://avatars.githubusercontent.com/sangyunpark99?s=80" width="80px;" alt="sangyunpark99"/><br/>
<b>sangyunpark99</b><br/>
박상윤
</a>
<br/><br/>
<a href="https://github.com/chungjeongsu">
<img src="https://avatars.githubusercontent.com/chungjeongsu?s=80" width="80px;" alt="chungjeongsu"/><br/>
<b>chungjeongsu</b><br/>
정지호
</a>
</td>
</tr>
</table>
</div>

## 프로젝트 배경

### 문제 인식

현재 LG U+ 챗봇(홀맨)은 대부분 정형화된 응답 스크립트에 의존하고 있으며, 고객의 세부적인 통신 사용 패턴이나 선호도를 반영하지 못하는 룰 기반의 방식을 취하고 있습니다. 이로 인해 고객은 본인에게 적합하지 않은 요금제를 안내받거나, 관련없는 정보를 받는 등 불편을 겪고 있습니다.

### 해결 목표

본 프로젝트는 제시된 요구사항과 더불어 LG U+ 고객 대상 챗봇 서비스를 구현하여, 사용자의 통신 사용 패턴과 선호도를 반영한 맞춤형 요금제 추천과 간단한 질의응답 용례 기능을 제공하는 것을 목표로 합니다.

### 기대 효과

이에 따라 페르소나는 "나에게 맞는 요금제를 추천받고 싶은 고객"으로 제한하여 설정했으며, 해당 사용자에게 실제로 기존 홀맨의 응답에서 나아가 자연스럽고 정확한 응답을 제공하는 것에 목적이 있습니다.

## ERD
![image](https://github.com/user-attachments/assets/42f0310c-f8bd-431b-b874-05a7f48a0d7e)


## 서비스 요청 흐름도

![image](https://github.com/user-attachments/assets/c4377fa8-2b38-4ee5-a863-6bfdcd91d156)


## 시스템 아키텍처
<img width="996" alt="image" src="https://github.com/user-attachments/assets/877d9fb9-fb1d-4fd7-9bd1-f5e087ec3f39" />



## 기능 소개

### 1️⃣ 로그인
👉 IFMUNEO 유저는 ID/PW로 로그인할 수 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/dbd9414d-92ec-455b-8baa-95a8ea432d54" width="200" height="260"/>
<img src = "https://github.com/user-attachments/assets/2ccd1431-5ef6-4339-a7e0-c4336476317e" width="200" height="260"/>
<br>


### 2️⃣ 메인
👉 IFMUNEO의 메인에서는 주요 요금제와 제공하고 있는 부가서비스를 한눈에 확인할 수 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/307fbadb-907f-4b3b-b119-8ea4333af87b" width="300" height="360"/>
<br>

### 3️⃣ 챗봇
👉 챗봇 무너를 이용하면 24시간 내내 요금제와 관련하여 궁금한 사항을 물어볼 수 있습니다.<br>
👉 LG U+에서 제공하는 요금제를 추천받을 수도 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/eda26f91-3400-45cb-8bd2-70bf2ebaae61" width="200" height="290"/>
<img src = "https://github.com/user-attachments/assets/a6b9d3b8-31b7-4a4d-a98d-53d060d334f9" width="200" height="290"/>
<br>
<br>

![image](https://github.com/user-attachments/assets/c230d86f-2ed3-4bc2-87c2-540e3baaf9bb)


### 4️⃣ 요금제 모아보기
👉 요금제 모아보기를 통해 LG U+에서 제공하고 있는 다양한 요금제를 확인할 수 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/f3ca3d23-a5ad-437e-8c2e-d688d2a7ad5c" width="300" height="390"/>

<br>

![image](https://github.com/user-attachments/assets/70dc14f4-59ab-4523-99bc-e29466c40f9a)




### 5️⃣ 요금제 상세보기
👉 요금제 모아보기 페이지에서 각 요금제를 클릭하면 해당 요금제의 세세한 정보를 확인할 수 있습니다.
👉 해당 요금제를 이용한 사용자가 남긴 리뷰를 확인할 수 있으며, 로그인한 사용자가 사용하고 있는 요금제일 경우 직접 리뷰를 남길 수 있습니다.

<br>
(화면 추후 공개)
<br>

![image](https://github.com/user-attachments/assets/58345058-43aa-479d-87f5-68a602042ffe)


### 6️⃣ 부가서비스 모아보기
👉 부가서비스 모아보기를 통해 LG U+에서 제공하고 있는 다양한 부가서비스를 확인할 수 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/5975b053-52ef-44c0-9df6-89d960d94c09" width="300" height="390"/>
<br>

![image](https://github.com/user-attachments/assets/c54e81e6-4e12-472b-8590-7fc71ce0457b)


### 7️⃣ 마이페이지
👉 마이페이지에서는 사용자의 정보를 확인할 수 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/04079729-1f9b-4994-9590-bad6d99f9b0e" width="400" height="230"/>
<br>

![image](https://github.com/user-attachments/assets/b4c68c0b-c731-48b2-a77d-f695c8f3ccf5)


### 8️⃣ 관리자 모드
👉 관리자는 ADMIN 페이지에서 요금제 데이터를 생성할 수 있습니다.

<br>
<img src = "https://github.com/user-attachments/assets/bfb4e44e-120e-4b96-ab19-590b93e06f99" width="400" height="230"/>
<br>

![image](https://github.com/user-attachments/assets/0bcb3292-5233-46e4-87a6-eded4f35a7b7)


### 9️⃣ (관리자만 해당) 요금제 · 부가서비스 생성
👉 관리자는 다음과 같은 순서로 요금제 데이터를 생성할 수 있습니다.

<br>
- 부가서비스 생성
<br>
<img src = "https://github.com/user-attachments/assets/1b9dc30b-861a-473c-8743-f5a58051fc79" width="400" height="130"/>
<br>

![image](https://github.com/user-attachments/assets/e306b222-8650-4ad2-aa5b-fea25b5036af)

![image](https://github.com/user-attachments/assets/d339b35b-4c42-4af8-b00d-90656409fedb)


- 부가서비스 그룹 생성
<br>
<img src = "https://github.com/user-attachments/assets/ff51982c-8629-4ac5-87ec-5a6132ddb59c" width="400" height="130"/>
<br>

![image](https://github.com/user-attachments/assets/a937345f-1175-46c9-894d-654131384dbf)
![image](https://github.com/user-attachments/assets/c08535c1-5278-4d01-9070-d84980c2757f)


- 요금제 정보 생성
<br>
<img src = "https://github.com/user-attachments/assets/25b315f2-d256-49e5-b01c-953b5f6fac2e" width="400" height="130"/>
<br>
- 요금제 생성
<br>
<img src = "https://github.com/user-attachments/assets/6d52cabd-50ac-4104-9b51-adf44a3a202d" width="400" height="300"/>
<br>

![image](https://github.com/user-attachments/assets/8ae65bf1-6d42-473a-8dcc-3327a5450f77)

![image](https://github.com/user-attachments/assets/5b11ae34-05c2-4f33-9dc2-ddcbc62410e2)


## 🛠️ Tech Stack

#### 📱 Frontend
<div align="center">

| React | TypeScript |
|:---:|:---:|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg" width="60" height="60"/> | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/typescript/typescript-original.svg" width="60" height="60"/> |

</div>

#### 🔧 Backend
<div align="center">

| Spring Boot | WebFlux | WebSocket | JUnit | JPA | JDBC |
|:---:|:---:|:---:|:---:|:---:|:---:|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="60" height="60"/> | <img src="https://img.shields.io/badge/WebFlux-6DB33F?style=flat&logo=spring&logoColor=white" width="60" height="30"/> | <img src="https://img.shields.io/badge/WebSocket-010101?style=flat&logo=socketdotio&logoColor=white" width="60" height="30"/> | <img src="https://img.shields.io/badge/JUnit-25A162?style=flat&logo=junit5&logoColor=white" width="60" height="30"/> | <img src="https://img.shields.io/badge/JPA-59666C?style=flat&logo=hibernate&logoColor=white" width="60" height="30"/> | <img src="https://img.shields.io/badge/JDBC-007396?style=flat&logo=java&logoColor=white" width="60" height="30"/> |

</div>

#### 🗄️ Database
<div align="center">

| PostgreSQL | pgvector |
|:---:|:---:|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="60" height="60"/> | <img src="https://img.shields.io/badge/pgvector-336791?style=flat&logo=postgresql&logoColor=white" width="60" height="30"/> |

</div>

#### ☁️ Infrastructure
<div align="center">

| Redis | OpenAI API | Docker | AWS EC2 | AWS RDS | AWS CloudFront |
|:---:|:---:|:---:|:---:|:---:|:---:|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/redis/redis-original.svg" width="60" height="60"/> | <img src="https://img.shields.io/badge/OpenAI-412991?style=flat&logo=openai&logoColor=white" width="60" height="30"/> | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" width="60" height="60"/> | <img src="https://img.shields.io/badge/AWS_EC2-FF9900?style=flat&logo=amazon-ec2&logoColor=white" width="60" height="30"/> | <img src="https://img.shields.io/badge/AWS_RDS-527FFF?style=flat&logo=amazon-rds&logoColor=white" width="60" height="30"/> | <img src="https://img.shields.io/badge/CloudFront-FF9900?style=flat&logo=amazon-aws&logoColor=white" width="60" height="30"/> |

</div>



## Team Rule

### 🌿 브랜치 관리

- **기본 구조**: `main` ← `develop` ← `feature/MUN-123-feature-name`
- **작업 절차**: develop에서 feature 브랜치 생성 → 개발 → PR 생성 → 코드 리뷰 → develop 머지
- **동기화 원칙**: 작업 시작 전과 푸시 전에 반드시 develop 최신화 후 merge
- **완료 기간**: 각 feature 브랜치는 1-3일 내 완료, develop 직접 커밋 금지

### 📝 커밋 컨벤션

```
MUN-[티켓번호] [타입]: [간단한 설명]
```

- **타입**: `feat`(기능), `fix`(버그), `refactor`(리팩토링), `docs`(문서), `test`(테스트), `chore`(기타)
- **예시**: `MUN-123 feat: 사용자 인증 기능 구현`

### 🔍 코드 리뷰

- **리뷰어**: 모든 PR에 3명 지정, 24시간 내 리뷰 완료
- **PR 크기**: 200줄 이하 권장, 효율적인 리뷰를 위한 적절한 단위 유지

### 💻 코딩 컨벤션

- **엔드포인트**: `/v1/chat` 형식
- **클래스명**: PascalCase, 역할별 suffix (`Controller`, `Service`, `Repository`)
- **메서드명**: camelCase, 의도가 명확한 네이밍
- **Bean 명명**: camelCase, 소문자 시작
- **멤버 순서**: 상수 → 인스턴스 변수 → 생성자 → public 메서드 → private 메서드

### ⚡ 개발 원칙

- **레이어 분리**: Controller → Service → Repository 계층 구조 준수
- **의존성 주입**: `@RequiredArgsConstructor` 활용한 생성자 주입
- **예외 처리**: 글로벌 예외 핸들러를 통한 일관된 에러 응답
- **테스트**: 비즈니스 로직에 대한 단위 테스트 필수 작성










