# ************************************************************
# Antares - SQL Client
# Version 0.7.20
# 
# https://antares-sql.app/
# https://github.com/antares-sql/antares
# 
# Host: 127.0.0.1 (MySQL Community Server - GPL 8.0.21)
# Database: antdv_pro
# Generation time: 2024-01-17T14:33:24+08:00
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


# Dump of table auth_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_permission`;

CREATE TABLE `auth_permission`
(
    `id`                    bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `key`                   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '必须唯一',
    `title`                 varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `icon`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `path`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '路由路径, 为空会使用key与父路由的key进行拼接',
    `component`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `redirect`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '重定向地址, 为空表示不重写重定向地址',
    `target`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `show`                  tinyint(1)                                                   NOT NULL DEFAULT '1',
    `hide_children`         tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否隐藏子菜单',
    `hidden_header_content` tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否隐藏隐藏面包屑和页面标题栏',
    `parent_id`             bigint                                                       NOT NULL COMMENT '父路由id',
    `type`                  tinyint                                                      NOT NULL DEFAULT '0' COMMENT '此菜单类型, 可选值: 0: 目录, 1: 模块, 2: 页面. ',
    `create_user`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `create_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_user`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `update_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `key` (`key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='菜单';

LOCK TABLES `auth_permission` WRITE;
/*!40000 ALTER TABLE `auth_permission`
    DISABLE KEYS */;

INSERT INTO `auth_permission` (`id`, `key`, `title`, `icon`, `path`, `component`, `redirect`, `target`, `show`,
                               `hide_children`, `hidden_header_content`, `parent_id`, `type`, `create_user`,
                               `update_user`)
VALUES (1, "root", "首页", NULL, "/", "BasicLayout", "/system", NULL, 1, 0, 0, 0, 0, "root", "root"),
       (2, "system", "系统管理", NULL, NULL, "BlankLayout", "/system/user", NULL, 1, 0, 0, 1, 0, "root", "root"),
       (3, "user", "用户管理", NULL, NULL, "PageView", "/system/user/list", NULL, 1, 1, 0, 2, 1, "root", "root"),
       (4, "userList", "用户列表", NULL, "/system/user/list", "UserList", NULL, NULL, 0, 0, 0, 3, 0, "root",
        "root"),
       (5, "role", "角色管理", NULL, NULL, "PageView", "/system/role/list", NULL, 1, 1, 0, 2, 1, "root", "root"),
       (6, "roleList", "角色列表", NULL, "/system/role/list", "RoleList", NULL, NULL, 1, 0, 0, 5, 2, "root",
        "root"),
       (7, "permission", "菜单管理", NULL, NULL, "PageView", "/system/permission/list", NULL, 1, 1, 0, 2, 1, "root",
        "root"),
       (8, "permissionList", "菜单列表", NULL, "/system/permission/list", "PermissionList", NULL,
        NULL, 1, 0, 0, 7, 2, "root", "root");

/*!40000 ALTER TABLE `auth_permission`
    ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table auth_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_role`;

CREATE TABLE `auth_role`
(
    `id`          bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_key`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `role_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_key` (`role_key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='角色表';

LOCK TABLES `auth_role` WRITE;
/*!40000 ALTER TABLE `auth_role`
    DISABLE KEYS */;

INSERT INTO `auth_role` (`id`, `role_key`, `role_name`, `description`, `create_user`, `update_user`)
VALUES (1, "SUPPER", "超级管理员", "超级管理员", "root", "root");

/*!40000 ALTER TABLE `auth_role`
    ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table auth_role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_role_permission`;

CREATE TABLE `auth_role_permission`
(
    `role_id`       bigint                                                        NOT NULL COMMENT '角色id',
    `permission_id` bigint                                                        NOT NULL COMMENT '菜单id',
    `actions`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限指令集',
    PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='角色权限关联';



# Dump of table auth_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_user`;

CREATE TABLE `auth_user`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
    `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '更新人',
    `last_login`  datetime                                                               DEFAULT NULL COMMENT '最后登录',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

LOCK TABLES `auth_user` WRITE;
/*!40000 ALTER TABLE `auth_user`
    DISABLE KEYS */;

INSERT INTO `auth_user` (`id`, `username`, `password`, `create_user`, `update_user`, `last_login`)
VALUES (1, "root", "$2a$10$X1eIjs1fZ3AUHqfVluxceuA1QOqorR6XvOKQ2XhHXrPkMkNmRF496", "root", "root",
        "2024-01-17 14:30:56");

/*!40000 ALTER TABLE `auth_user`
    ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table auth_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_user_role`;

CREATE TABLE `auth_user_role`
(
    `user_id` bigint NOT NULL COMMENT '用户id',
    `role_id` bigint NOT NULL COMMENT '角色id',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户角色关联';

LOCK TABLES `auth_user_role` WRITE;
/*!40000 ALTER TABLE `auth_user_role`
    DISABLE KEYS */;

INSERT INTO `auth_user_role` (`user_id`, `role_id`)
VALUES (1, 1);

/*!40000 ALTER TABLE `auth_user_role`
    ENABLE KEYS */;
UNLOCK TABLES;



# Dump of views
# ------------------------------------------------------------

# Creating temporary tables to overcome VIEW dependency errors


/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;

# Dump completed on 2024-01-17T14:33:24+08:00
