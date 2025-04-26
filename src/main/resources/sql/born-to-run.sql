# Tables
CREATE TABLE `crew`
(
    `id`       bigint(20)  NOT NULL AUTO_INCREMENT,
    `name`     varchar(255) DEFAULT NULL,
    `contents` text        NOT NULL COMMENT '크루 설명',
    `sns`      varchar(255) DEFAULT NULL,
    `image_id` bigint(20)   DEFAULT NULL COMMENT '크루 대표 이미지 식별자',
    `logo_id`  bigint(20)   DEFAULT NULL COMMENT '크루 로고 식별자',
    `region`   varchar(50) NOT NULL COMMENT '활동 지역',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `user`
(
    `id`              bigint(20)  NOT NULL AUTO_INCREMENT,
    `provider_type`   varchar(20) NOT NULL,
    `role_type`       varchar(20) NOT NULL,
    `social_id`       varchar(32) NOT NULL COMMENT '소셜의 회원 식별자',
    `name`            varchar(10)          DEFAULT NULL COMMENT '성명',
    `crew_id`         bigint(20)           DEFAULT NULL COMMENT '소속 크루 식별자',
    `image_id`        bigint(20)           DEFAULT NULL,
    `instagram_id`    varchar(255)         DEFAULT NULL,
    `yellow_card_qty` int(11)     NOT NULL DEFAULT 0 COMMENT '접수된 신고 중 허용된 신고 개수',
    `last_login_at`   datetime    NOT NULL DEFAULT current_timestamp() COMMENT '마지막 로그인 일자',
    `updated_at`      datetime    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `registered_at`   datetime    NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    KEY `idx__crew_id` (`crew_id`),
    KEY `idx__social_id` (`social_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `object_storage`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(20)   NOT NULL COMMENT '업로드한 사용자의 식별자',
    `file_uri`    varchar(255) NOT NULL COMMENT '파일의 cdn full uri',
    `bucket_name` varchar(32)  NOT NULL,
    `upload_at`   datetime     NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `activity_participation`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `activity_id`   bigint(20) NOT NULL COMMENT '행사 식별자',
    `user_id`       bigint(20) NOT NULL COMMENT '참석자',
    `registered_at` datetime   NOT NULL DEFAULT current_timestamp(),
    `updated_at`    datetime   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `is_attendance` tinyint(1) NOT NULL DEFAULT 0 COMMENT '출석여부',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk__activity_id__user_id` (`activity_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `user_privacy`
(
    `id`                     bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`                bigint(20) NOT NULL COMMENT '사용자',
    `is_instagram_id_public` tinyint(1) NOT NULL DEFAULT 0 COMMENT '인스타그램 아이디 노출 여부',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk__user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `feed_image_mapping`
(
    `feed_id`  bigint(20) NOT NULL COMMENT '피드 식별자',
    `image_id` bigint(20) NOT NULL COMMENT '이미지 식별자',
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `recommendation`
(
    `id`                  bigint(20)  NOT NULL AUTO_INCREMENT,
    `content_id`          bigint(20)  NOT NULL COMMENT '컨텐츠 식별자(boardId/commentId)',
    `user_id`             bigint(20)  NOT NULL COMMENT '좋아요 누른 사용자',
    `recommendation_type` varchar(15) NOT NULL COMMENT '좋아요 타입(board/comment)',
    `registered_at`       datetime    NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk__user_id__recommendation_type__content_id` (`user_id`, `recommendation_type`, `content_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `comment`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `parent_id`     bigint(20) NULL COMMENT '부모 댓글 식별자(대댓글)',
    `user_id`       bigint(20) NOT NULL COMMENT '작성자',
    `feed_id`       bigint(20)          DEFAULT NULL,
    `contents`      text       NOT NULL COMMENT '내용',
    `registered_at` datetime   NOT NULL DEFAULT current_timestamp(),
    `updated_at`    datetime   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`id`),
    KEY `idx__feed_id` (`feed_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `user_refresh_token`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_id`       bigint(20)   NOT NULL,
    `refresh_token` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `feed`
(
    `id`            bigint(20)  NOT NULL AUTO_INCREMENT,
    `user_id`       bigint(20)  NOT NULL COMMENT '작성자',
    `contents`      text        NOT NULL COMMENT '내용',
    `category`      varchar(50) NOT NULL COMMENT '게시판 종류',
    `access_level`  varchar(50) NOT NULL DEFAULT 'ALL' COMMENT '공개 범위',
    `view_qty`      int(11)     NOT NULL DEFAULT 0 COMMENT '조회수',
    `registered_at` datetime    NOT NULL DEFAULT current_timestamp(),
    `updated_at`    datetime    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`id`),
    KEY `idx__category__id` (`category`, `id`),
    KEY `idx__category__id__user_id` (`category`, `id`, `user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `yellow_card`
(
    `target_user_id` bigint(20) NOT NULL,
    `source_user_id` bigint(20) NOT NULL COMMENT '신고한 사람',
    `reason`         text DEFAULT NULL COMMENT '신고 사유',
    `basis`          text       NOT NULL COMMENT '신고 컨텐츠 내용',
    PRIMARY KEY (`target_user_id`, `source_user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `activity`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
    `title`              varchar(225) NOT NULL COMMENT '행사 명칭',
    `contents`           text         NOT NULL COMMENT '내용',
    `start_date`         datetime     NOT NULL COMMENT '행사 시작 일자',
    `venue`              varchar(125)          DEFAULT NULL COMMENT '장소',
    `venue_url`          varchar(125) NOT NULL COMMENT '장소의 지도 url',
    `participants_limit` int(11)               DEFAULT NULL COMMENT '인원제한 (제한없음: -1)',
    `participation_fee`  int(11)      NOT NULL DEFAULT 0 COMMENT '참가비',
    `course`             varchar(32)           DEFAULT NULL COMMENT '코스(km)',
    `course_detail`      varchar(125)          DEFAULT NULL COMMENT '코스 설명',
    `path`               varchar(225)          DEFAULT NULL COMMENT '경로',
    `user_id`            bigint(20)   NOT NULL COMMENT '호스트',
    `is_open`            tinyint(1)   NOT NULL DEFAULT 0 COMMENT '행사 오픈 여부',
    `updated_at`         datetime     NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `registered_at`      datetime     NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk__start_date__user_id` (`start_date`, `user_id`),
    KEY `idx__course` (`course`),
    KEY `idx__venue__course` (`venue`, `course`),
    KEY `idx__venue` (`venue`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `marathon_bookmark`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`       bigint(20) NOT NULL COMMENT '유저 식별자',
    `marathon_id`   bigint(20) NOT NULL COMMENT '대회 식별자',
    `registered_at` datetime   NOT NULL DEFAULT current_timestamp(),
    `updated_at`    datetime   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id__marathon_id` (`user_id`, `marathon_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `activity_image_mapping`
(
    `activity_id` bigint(20) NOT NULL COMMENT '행사 식별자',
    `image_id`    bigint(20) NOT NULL COMMENT '이미지 식별자',
    PRIMARY KEY (`activity_id`, `image_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `marathon`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `title`         varchar(1024) DEFAULT NULL COMMENT '대회명',
    `owner`         varchar(1024) DEFAULT NULL COMMENT '대표자명',
    `email`         varchar(1024) DEFAULT NULL COMMENT 'E-mail',
    `schedule`      varchar(1024) DEFAULT NULL COMMENT '대회일시',
    `contact`       varchar(1024) DEFAULT NULL COMMENT '전화번호',
    `course`        varchar(256)  DEFAULT NULL COMMENT '대회종목',
    `location`      varchar(256)  DEFAULT NULL COMMENT '대회지역',
    `venue`         varchar(1024) DEFAULT NULL COMMENT '대회장소',
    `host`          varchar(1024) DEFAULT NULL COMMENT '주최단체',
    `duration`      varchar(1024) DEFAULT NULL COMMENT '접수기간',
    `homepage`      varchar(1024) DEFAULT NULL COMMENT '홈페이지',
    `venue_detail`  varchar(1024) DEFAULT NULL COMMENT '대회장',
    `remark`        text          DEFAULT NULL COMMENT '기타소개',
    `registered_at` datetime   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk__title__owner` (`title`, `owner`) USING HASH,
    KEY `idx__location__course` (`location`, `course`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 521
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

########################################################################################################################

# Constraints


ALTER TABLE user
    ADD CONSTRAINT fk__user__crew
        FOREIGN KEY (crew_id) REFERENCES crew (id) ON DELETE SET NULL;
ALTER TABLE user
    ADD CONSTRAINT fk__user__object_storage
        FOREIGN KEY (image_id) REFERENCES object_storage (id) ON DELETE SET NULL;

ALTER TABLE user_privacy
    ADD CONSTRAINT fk__user_privacy__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE crew
    ADD CONSTRAINT fk__crew__object_storage_image
        FOREIGN KEY (image_id) REFERENCES object_storage (id) ON DELETE SET NULL;
ALTER TABLE crew
    ADD CONSTRAINT fk__crew__object_storage_logo
        FOREIGN KEY (logo_id) REFERENCES object_storage (id) ON DELETE SET NULL;

ALTER TABLE object_storage
    ADD CONSTRAINT fk__object_storage__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE activity
    ADD CONSTRAINT fk__activity__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE activity_image_mapping
    ADD CONSTRAINT fk__activity_image_mapping__activity
        FOREIGN KEY (activity_id) REFERENCES activity (id) ON DELETE CASCADE;
ALTER TABLE activity_image_mapping
    ADD CONSTRAINT fk__activity_image_mapping__object_storage
        FOREIGN KEY (image_id) REFERENCES object_storage (id) ON DELETE CASCADE;

ALTER TABLE activity_participation
    ADD CONSTRAINT fk__activity_participation__activity
        FOREIGN KEY (activity_id) REFERENCES activity (id) ON DELETE CASCADE;
ALTER TABLE activity_participation
    ADD CONSTRAINT fk__activity_participation__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE feed_image_mapping
    ADD CONSTRAINT fk__feed_image_mapping__object_storage
        FOREIGN KEY (image_id) REFERENCES object_storage (id) ON DELETE CASCADE;

ALTER TABLE recommendation
    ADD CONSTRAINT fk__recommendation__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE feed
    ADD CONSTRAINT fk__feed__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT fk__comment__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;
ALTER TABLE comment
    ADD CONSTRAINT fk__comment__self
        FOREIGN KEY (parent_id) REFERENCES comment (id) ON DELETE CASCADE;
ALTER TABLE comment
    ADD CONSTRAINT fk__comment__feed
        FOREIGN KEY (feed_id) REFERENCES feed (id) ON DELETE CASCADE;

ALTER TABLE yellow_card
    ADD CONSTRAINT fk__yellow_card__user_target
        FOREIGN KEY (target_user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE yellow_card
    ADD CONSTRAINT fk__yellow_card__user_source
        FOREIGN KEY (source_user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE marathon_bookmark
    ADD CONSTRAINT fk__marathon_bookmark__user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;
ALTER TABLE marathon_bookmark
    ADD CONSTRAINT fk__marathon_bookmark__marathon
        FOREIGN KEY (marathon_id) REFERENCES marathon (id) ON DELETE CASCADE;

