웨딩맵
===

DND 8기 8조의 웨딩맵 프로젝트입니다.

API 문서
---

API 문서는 [해당 페이지](https://dnd-side-project.github.io/dnd-8th-8-backend/)에서 확인해 볼 수 있습니다.
`Spring Rest Docs`를 이용하여 테스트 코드가 통과한 API에 대한 문서를 자동으로 생성하고 있습니다.

시작하기
---

프로젝트를 로컬에서 실행시키는 방법은 다음과 같습니다.

> **Note** : 프로젝트를 실행시키기 위해서는 `Java 17`이 설치되어 있어야 합니다.

### 1. 프로젝트 클론

```bash
$ git clone https://github.com/dnd-side-project/dnd-8th-8-backend.git
```

### 2. 환경변수 설정

프로젝트 실행에 필요한 환경변수를 다음 파일을 참고하여 설정합니다.

- `src/main/resources/application.yml`
- `src/main/resources/application-local.yml`

### 3. 도커컴포즈 실행

```bash
$ docker-compose up -d
```

해당 명령어는 `docker-compose.yml`에 정의된 컨테이너를 실행시킵니다.

- `mysql` : MySQL 8.0.23
- `redis` : Redis 6.2.4

### 4. 프로젝트 빌드

```bash
$ ./gradlew build
```

웨딩맵 프로젝트를 빌드합니다.

### 5. 프로젝트 실행

```bash
$ ./gradlew bootRun --args='--spring.profiles.active=local'
```

웨딩맵 프로젝트를 `local` 환경에서 실행합니다.
