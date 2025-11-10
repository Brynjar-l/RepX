-- =====================================================
--  RepX full schema reset
--  SAFE: Drops all public tables and recreates clean schema
-- =====================================================

-- DROP EVERYTHING (tables, sequences, views)
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
        EXECUTE 'DROP TABLE IF EXISTS public.' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END$$;

-- =====================================================
-- EXTENSIONS
-- =====================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "citext";

-- =====================================================
-- USERS
-- =====================================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email CITEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    display_name TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- =====================================================
-- EXERCISES
-- =====================================================
CREATE TABLE exercises (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL UNIQUE,
    primary_muscle TEXT,
    equipment TEXT,
    difficulty TEXT,
    is_public BOOLEAN NOT NULL DEFAULT TRUE
);

-- =====================================================
-- WORKOUTS
-- =====================================================
CREATE TABLE workouts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ,
    notes TEXT
);

-- =====================================================
-- WORKOUT_EXERCISES
-- =====================================================
CREATE TABLE workout_exercises (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_id UUID NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id UUID NOT NULL REFERENCES exercises(id),
    order_index INT NOT NULL DEFAULT 0
);

-- =====================================================
-- LIFT_SETS
-- =====================================================
CREATE TABLE sets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_exercise_id UUID NOT NULL REFERENCES workout_exercises(id) ON DELETE CASCADE,
    set_index INT NOT NULL,
    reps INT,
    weight_kg NUMERIC(6,2),
    rir INT,
    duration_sec INT,
    notes TEXT
);

-- =====================================================
-- TEMPLATES
-- =====================================================
CREATE TABLE templates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    notes TEXT,
    CONSTRAINT uq_template_name_per_user UNIQUE (user_id, name)
);

-- =====================================================
-- TEMPLATE_EXERCISES
-- =====================================================
CREATE TABLE template_exercises (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    template_id UUID NOT NULL REFERENCES templates(id) ON DELETE CASCADE,
    exercise_id UUID NOT NULL REFERENCES exercises(id),
    order_index INT NOT NULL DEFAULT 0,
    default_reps INT,
    default_weight_kg NUMERIC(6,2)
);

-- =====================================================
-- PROGRESS_PHOTOS (optional future table)
-- =====================================================
CREATE TABLE progress_photos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    media_key TEXT NOT NULL,
    taken_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_workouts_user_id ON workouts(user_id);
CREATE INDEX idx_workout_exercises_workout_id ON workout_exercises(workout_id);
CREATE INDEX idx_sets_workout_exercise_id ON sets(workout_exercise_id);
CREATE INDEX idx_templates_user_id ON templates(user_id);
CREATE INDEX idx_template_exercises_template_id ON template_exercises(template_id);

-- =====================================================
-- DONE
-- =====================================================
COMMENT ON DATABASE current_database() IS 'RepX clean schema installed successfully.';
