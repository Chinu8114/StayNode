# StayNode — Production-Ready PG & Hostel Platform

Full-stack app: HTML/JS frontend, Spring Boot REST API, MySQL, served behind nginx.
Real property images, JWT auth, env-based config, single-origin deployment.

## Stack

- **Frontend** — single-file HTML/CSS/JS app served by **nginx (alpine)** with gzip + asset caching.
- **API** — **Java 21 + Spring Boot 3.3** (Web, JPA, Security, Validation), JWT (JJWT 0.12).
- **DB** — **MySQL 8.4** with persisted volume, auto-seeded demo data on first boot.
- **Reverse proxy** — nginx forwards `/api/*` to the Spring Boot service so the whole app
  is served from a single origin (no CORS issues in production).
- **Container orchestration** — Docker Compose, health-checked, restart on failure.

## Run the whole project with one command

```bash
cd staynode-backend
docker compose up --build
```

Open **http://localhost:8080** — that's it. Frontend, API, and database are all live.

| Service  | URL / Port                          | Notes                               |
|----------|-------------------------------------|-------------------------------------|
| Frontend | http://localhost:8080               | Static HTML served by nginx         |
| API      | http://localhost:8080/api/health    | Reverse-proxied through nginx       |
| MySQL    | localhost:3307 (mapped from 3306)   | App-internal hostname is `mysql`    |

> The API is **not** exposed directly on the host — only nginx is. This is the
> production pattern: one public port, single origin, no CORS surprises.

To stop everything:

```bash
docker compose down          # keeps the database
docker compose down -v       # wipes the database too
```

## Production configuration

Copy `.env.example` to `.env` and fill in real values. Compose picks it up
automatically.

```bash
cp .env.example .env
# Generate a strong JWT secret:
openssl rand -base64 48
```

Key variables you should change before going live:

| Var                   | What it controls                                      |
|-----------------------|-------------------------------------------------------|
| `JWT_SECRET`          | **Must be ≥ 32 bytes**. Anyone with this can forge tokens. |
| `MYSQL_ROOT_PASSWORD` | MySQL root password                                   |
| `DB_PASSWORD`         | App DB user password                                  |
| `CORS_ORIGINS`        | Comma-separated list of allowed origins (e.g. `https://staynode.example.com`). Use `*` only in dev. |
| `WEB_HOST_PORT`       | Public port nginx listens on (default `8080`)         |
| `MYSQL_HOST_PORT`     | Host port for MySQL (default `3307`)                  |
| `SPRING_PROFILE`      | `prod` (default) or `default`                         |

The `prod` Spring profile (`application-prod.yml`) hides stack traces from
clients, lowers log verbosity, enables HTTP compression, and disables SQL echo.

## Demo accounts (password: `demo123`)

| Email             | Role  |
|-------------------|-------|
| user@demo.com     | USER  |
| owner@demo.com    | OWNER |
| priya@demo.com    | OWNER |
| meera@demo.com    | OWNER |
| arun@demo.com     | OWNER |
| admin@demo.com    | ADMIN |

## API endpoints

All endpoints are prefixed with `/api`. Authenticated endpoints require
`Authorization: Bearer <jwt>`.

### Auth
- `POST /api/auth/register` — `{ name, email, password, phone?, role? }` → `{ user, token }`
- `POST /api/auth/login` — `{ email, password }` → `{ user, token }`
- `GET  /api/auth/me` — current user

### Properties (public reads)
- `GET /api/properties/featured`
- `GET /api/properties?city=&type=PG|Hostel|Flat&gender=MALE|FEMALE|ANY&minPrice=&maxPrice=&minRating=`
- `GET /api/properties/{id}`

### Wishlist (auth)
- `POST /api/properties/{propertyId}/wishlist` — toggles add/remove
- `GET  /api/wishlist`

### Bookings (auth)
- `POST /api/bookings` — `{ propertyId, checkIn, checkOut, room? }`
- `GET  /api/bookings` — current user's bookings

### Health
- `GET /api/health`

## Real images

Properties ship with curated Unsplash photos. The frontend renders real `<img>`
tags when an image looks like a URL and gracefully falls back to the gradient/emoji
placeholder if a URL fails to load. Add your own images by editing
`DataSeeder.java` (each property has an `images(...)` list of URLs).

## Project layout

```
staynode-backend/
├── docker-compose.yml      # one-command stack
├── Dockerfile              # multi-stage Java build
├── nginx.conf              # nginx config + /api reverse proxy
├── .env.example            # production env template
├── frontend/index.html     # the StayNode UI
├── pom.xml
└── src/main/
    ├── java/com/staynode/
    │   ├── StayNodeApplication.java
    │   ├── auth/        # login, register, /me
    │   ├── booking/     # bookings
    │   ├── common/      # health, error handling
    │   ├── config/      # CORS, seed data
    │   ├── property/    # properties + search
    │   ├── security/    # JWT filter & service
    │   ├── user/        # User entity
    │   └── wishlist/    # wishlist toggle
    └── resources/
        ├── application.yml
        └── application-prod.yml
```

## Going further

- **HTTPS:** put a TLS-terminating proxy (Caddy, Cloudflare, or nginx with certbot)
  in front of the `web` container. Then set `CORS_ORIGINS` to your real domain.
- **Persisted images:** mount object storage (S3, GCS) for user-uploaded property
  photos and store URLs in the existing `property_images` table.
- **Schema migrations:** swap `ddl-auto: update` for Flyway or Liquibase before
  the schema stabilises in production.
