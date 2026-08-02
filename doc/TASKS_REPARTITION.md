# Cine-app

## 1. What Has Already Been Established

These elements constitute the shared **contract**: no one should modify them without a
team review, because the rest of the codebase depends on them.

* `src/main/java/school/hei/cineapp/model/*` — Room, Seat, Movie, Genre, Projection,
  Reservation, User, UserWithToken, RegisterPayload, LoginPayload
* `src/main/java/school/hei/cineapp/security/model/UserRole.java` — CLIENT, EMPLOYEE, MANAGER
* `doc/api.yml` — OpenAPI specification (endpoints, schemas, expected response codes)

> Any discrepancy discovered during development (missing field, new endpoint, etc.)
> must first be proposed by modifying these files, then discussed with the team,
> before implementing any code that depends on it.

### Cine-app Specific Rules (based on the provided requirements table)

* `PUT /movies`, `PUT /projection`, `PUT /rooms`, `PUT /rooms/{id}/seats` → **MANAGER only** (otherwise return 403)
* `GET /reservations` → **EMPLOYEE + MANAGER only** (403 for CLIENT)
* `GET /reservations/{id}` → CLIENT is authorized **only if they own the reservation**; EMPLOYEE and MANAGER are always authorized.
    * This rule cannot be enforced with a simple `SelfAuthorizationManager` on the URL
      (there is no `{uid}` path variable). Ownership must instead be verified **inside the service**,
      following the same pattern as `CrupdateTodoValidator`, which throws an `AccessDeniedException`.
* `PUT /reservation` → EMPLOYEE/MANAGER (reservation made at the ticket office)
* `PUT /users/{uid}/reservations` → CLIENT can make reservations for themselves only,
  using the same `SelfAuthorizationManager` pattern as `/users/{uid}/todos`
* `GET /projections`, `GET /movies` → Publicly accessible, including anonymous users

# 2. Work Split

### Work Package A — Catalog (David)

Domains: **Room, Seat, Movie, Projection**

* [ ] `JRoom`, `JSeat`, `JMovie`, `JProjection` (JPA entities) + mappers
* [ ] Flyway migrations (`db/migration/V1_x__create_*_table.sql`)
* [ ] `RoomService`, `SeatService`, `MovieService`, `ProjectionService`
* [ ] `CrupdateMovieValidator`, `CrupdateProjectionValidator`, `CrupdateRoomValidator`
  (title/number are required, `projection.movieId` and `roomId` must exist, etc.)
* [ ] Controllers: `MovieController`, `RoomController`, `SeatController`, `ProjectionController`
* [ ] Configure RBAC in `SecurityConf` for these routes (MANAGER-only for write operations, public for read operations)
* [ ] Integration tests (Testcontainers): cover the 200/403 scenarios from the requirements table for movies, projections, and rooms

### Work Package B — Accounts & Reservations (Fenohasina)

Domains: **User (authentication/JWT), Reservation**

* [ ] `JUser`, `JReservation` (JPA entities) + mappers
* [ ] Flyway migrations for `user` and `reservation`
* [ ] `UserService` (register/login, Argon2 password hashing, JWT — copy `JwtService`, `SecurityConf`,
  `BearerAuthFilter`, and `SelfAuthorizationManager` from TodoIt almost as-is)
* [ ] `ReservationService` with:

    * Ownership validation for `GET /reservations/{id}` (403 if the requester is neither the owner nor a staff member)
    * Uniqueness rule on `(projectionId, seatId)` — prevent double-booking the same seat
* [ ] Controllers: `AuthController`, `ReservationController`, `UserReservationController`
* [ ] Integration tests: register/login, reservation RBAC (the 5 scenarios from the requirements table), self-service booking

# 3. Git Workflow

* One branch per ticket: `feat/reservation-service`, `feat/movie-controller`, etc.
* **Conventional Commits** are mandatory: `feat:`, `fix:`, `test:`, `refactor:`, `chore:`, `docs:`

    * Example: `feat(reservation): add ownership check on GET /reservations/{id}`
* One commit = one clear intention. Avoid lingering commits such as "wip" or "fix typo":
  use `git commit --amend` or `git rebase -i` to squash or rename commits before opening a PR.
* Every PR must be reviewed before being merged.
* Merge into `main` using **Squash and Merge** to keep a clean commit history.
  The squash commit message must follow the Conventional Commit format and summarize the implemented feature.

# 4. Definition of Done (Both Work Packages)

* [ ] Successful Gradle build (`./gradlew build`)
* [ ] Jacoco coverage ≥ 80% for the modified classes
* [ ] At least one integration test (Testcontainers) for each RBAC rule defined in the requirements table
* [ ] `doc/api.yml` updated if an endpoint's contract has changed
* [ ] PR reviewed and approved before merge
