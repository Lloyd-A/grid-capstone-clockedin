CREATE DATABASE clocked_in;
\c clocked_in;


DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'locations') THEN
        CREATE TABLE "locations" (
                                     "location_id" UUID,
                                     "room_name" VARCHAR(255),
                                     "virtual_link" VARCHAR(100),
                                     PRIMARY KEY ("location_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'shifts') THEN
        CREATE TABLE "shifts" (
                                  "shift_id" UUID,
                                  "start_time" TIMESTAMP,
                                  "end_time" TIMESTAMP,
                                  "location_id" UUID,
                                  "course_enum" VARCHAR(20),
                                  PRIMARY KEY ("shift_id"),
                                  CONSTRAINT "FK_shifts.location_id"
                                      FOREIGN KEY ("location_id")
                                          REFERENCES "locations"("location_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'users') THEN
        CREATE TABLE "users" (
                                 "user_id" UUID,
                                 "university_id" VARCHAR(20),
                                 "first_name" VARCHAR(100),
                                 "last_name" VARCHAR(100),
                                 "user_role" VARCHAR(20),
                                 "phone_num" VARCHAR(20),
                                 "email" VARCHAR(20),
                                 PRIMARY KEY ("user_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'labtechs') THEN
        CREATE TABLE "labtechs" (
                                    "user_id" UUID,
                                    PRIMARY KEY ("user_id"),
                                    CONSTRAINT "FK_labtechs.user_id"
                                        FOREIGN KEY ("user_id")
                                            REFERENCES "users"("user_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'shifts_labtechs') THEN
        CREATE TABLE "shifts_labtechs" (
                                           "shift_id" UUID,
                                           "user_id" UUID,
                                           PRIMARY KEY ("shift_id", "user_id"),
                                           CONSTRAINT "FK_shifts_labtechs.shift_id"
                                               FOREIGN KEY ("shift_id")
                                                   REFERENCES "shifts"("shift_id"),
                                           CONSTRAINT "FK_shifts_labtechs.user_id"
                                               FOREIGN KEY ("user_id")
                                                   REFERENCES "users"("user_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'abstract_requests') THEN
        CREATE TABLE "abstract_requests" (
                                             "request_id" UUID,
                                             "requestor_id" UUID,
                                             "reason" VARCHAR(255),
                                             "request_status" VARCHAR(255),
                                             "time_created" TIMESTAMP,
                                             "time_resolved" TIMESTAMP,
                                             PRIMARY KEY ("request_id"),
                                             CONSTRAINT "FK_abstract_requests.reqestor_id"
                                                 FOREIGN KEY ("requestor_id")
                                                     REFERENCES "users"("user_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'request_approvers') THEN
        CREATE TABLE "request_approvers" (
                                             "user_id" UUID,
                                             "request_id" UUID,
                                             PRIMARY KEY ("request_id", "user_id"),
                                             CONSTRAINT "FK_request_approvers.request_id"
                                                 FOREIGN KEY ("request_id")
                                                     REFERENCES "abstract_requests"("request_id"),
                                             CONSTRAINT "FK_request_approvers.user_id"
                                                 FOREIGN KEY ("user_id")
                                                     REFERENCES "users"("user_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'shift_swap_requests') THEN
        CREATE TABLE "shift_swap_requests" (
                                               "request_id" UUID,
                                               "requested_shift_id" UUID,
                                               "proposed_shift_id" UUID,
                                               PRIMARY KEY ("request_id"),
                                               CONSTRAINT "FK_shift_swap_requests.request_id"
                                                   FOREIGN KEY ("request_id")
                                                       REFERENCES "abstract_requests"("request_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'time_off_requests') THEN
        CREATE TABLE "time_off_requests" (
                                             "request_id" UUID,
                                             "start_date" TIMESTAMP,
                                             "end_date" TIMESTAMP,
                                             PRIMARY KEY ("request_id"),
                                             CONSTRAINT "FK_time_off_requests.request_id"
                                                 FOREIGN KEY ("request_id")
                                                     REFERENCES "abstract_requests"("request_id")
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_tables WHERE schemaname = 'public' AND tablename = 'labmanagers') THEN
        CREATE TABLE "labmanagers" (
                                       "user_id" UUID,
                                       PRIMARY KEY ("user_id"),
                                       CONSTRAINT "FK_labmanagers.user_id"
                                           FOREIGN KEY ("user_id")
                                               REFERENCES "users"("user_id")
        );
    END IF;
END $$;