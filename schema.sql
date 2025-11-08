CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS citext;

-- ---------- users ----------
CREATE TABLE IF NOT EXISTS users (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email         CITEXT NOT NULL UNIQUE,
  password_hash TEXT   NOT NULL,
  display_name  TEXT   NOT NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------- exercises ----------
CREATE TABLE IF NOT EXISTS exercises (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name           TEXT NOT NULL UNIQUE,
  primary_muscle TEXT,
  equipment      TEXT,
  difficulty     TEXT,
  is_public      BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------- workouts ----------
CREATE TABLE IF NOT EXISTS workouts (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id    UUID NOT NULL REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
  title      TEXT NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  end_time   TIMESTAMPTZ,
  notes      TEXT
);

CREATE INDEX IF NOT EXISTS idx_workouts_user_time ON workouts(user_id, start_time DESC);

-- ---------- workout_exercises ----------
CREATE TABLE IF NOT EXISTS workout_exercises (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  workout_id  UUID NOT NULL REFERENCES workouts(id)  ON UPDATE CASCADE ON DELETE CASCADE,
  exercise_id UUID NOT NULL REFERENCES exercises(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  order_index INT  NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_wex_workout   ON workout_exercises(workout_id, order_index);
CREATE INDEX IF NOT EXISTS idx_wex_exercise  ON workout_exercises(exercise_id);

-- ---------- sets ----------
CREATE TABLE IF NOT EXISTS sets (
  id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  workout_exercise_id  UUID NOT NULL REFERENCES workout_exercises(id) ON UPDATE CASCADE ON DELETE CASCADE,
  set_index            INT  NOT NULL DEFAULT 1 CHECK (set_index >= 1),
  reps                 INT  CHECK (reps IS NULL OR reps >= 0),
  weight_kg            NUMERIC(6,2) CHECK (weight_kg IS NULL OR weight_kg >= 0),
  rir                  INT  CHECK (rir IS NULL OR (rir >= 0 AND rir <= 10)),
  duration_sec         INT  CHECK (duration_sec IS NULL OR duration_sec >= 0),
  notes                TEXT
);

CREATE INDEX IF NOT EXISTS idx_sets_wex ON sets(workout_exercise_id, set_index);

-- ---------- templates ----------
CREATE TABLE IF NOT EXISTS templates (
  id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id   UUID NOT NULL REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
  name      TEXT NOT NULL,
  notes     TEXT,
  CONSTRAINT uq_template_name_per_user UNIQUE (user_id, name)
);

-- ---------- template_exercises ----------
CREATE TABLE IF NOT EXISTS template_exercises (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  template_id      UUID NOT NULL REFERENCES templates(id) ON UPDATE CASCADE ON DELETE CASCADE,
  exercise_id      UUID NOT NULL REFERENCES exercises(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  order_index      INT NOT NULL DEFAULT 0,
  default_reps     INT CHECK (default_reps IS NULL OR default_reps >= 0),
  default_weight_kg NUMERIC(6,2) CHECK (default_weight_kg IS NULL OR default_weight_kg >= 0)
);

CREATE INDEX IF NOT EXISTS idx_tex_template ON template_exercises(template_id, order_index);

-- ---------- favorite_exercises (junction) ----------
CREATE TABLE IF NOT EXISTS favorite_exercises (
  user_id     UUID NOT NULL REFERENCES users(id)     ON UPDATE CASCADE ON DELETE CASCADE,
  exercise_id UUID NOT NULL REFERENCES exercises(id) ON UPDATE CASCADE ON DELETE CASCADE,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
  PRIMARY KEY (user_id, exercise_id)
);

-- ---------- body_metrics ----------
CREATE TABLE IF NOT EXISTS body_metrics (
  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id      UUID NOT NULL REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
  recorded_at  TIMESTAMPTZ NOT NULL,
  weight_kg    NUMERIC(6,2),
  body_fat_pct NUMERIC(5,2) CHECK (body_fat_pct IS NULL OR (body_fat_pct >= 0 AND body_fat_pct <= 100)),
  chest_cm     NUMERIC(6,2),
  waist_cm     NUMERIC(6,2),
  hips_cm      NUMERIC(6,2)
);

CREATE INDEX IF NOT EXISTS idx_body_metrics_user_time ON body_metrics(user_id, recorded_at DESC);

-- ---------- progress_photos ----------
CREATE TABLE IF NOT EXISTS progress_photos (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id    UUID NOT NULL REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
  taken_at   TIMESTAMPTZ NOT NULL,
  caption    TEXT,
  media_key  TEXT NOT NULL,
  mime_type  TEXT NOT NULL,
  size_bytes BIGINT CHECK (size_bytes IS NULL OR size_bytes >= 0)
);

CREATE INDEX IF NOT EXISTS idx_photos_user_time ON progress_photos(user_id, taken_at DESC);

CREATE OR REPLACE VIEW v_set_volume AS
SELECT
  s.workout_exercise_id,
  SUM(COALESCE(s.reps,0) * COALESCE(s.weight_kg,0))::NUMERIC(12,2) AS total_volume
FROM sets s
GROUP BY s.workout_exercise_id;

CREATE OR REPLACE VIEW v_recent_workouts AS
SELECT w.*
FROM workouts w
ORDER BY w.start_time DESC;
