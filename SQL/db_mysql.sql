SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for gar_log
-- ----------------------------
DROP TABLE IF EXISTS `gar_log`;gar_user_synchronize
CREATE TABLE `gar_log` (
  `id`           BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username`     VARCHAR(50)         DEFAULT NULL
  COMMENT '用户名',
  `operation`    VARCHAR(50)         DEFAULT NULL
  COMMENT '用户操作',
  `method`       VARCHAR(50)         DEFAULT NULL
  COMMENT '请求方法',
  `url`          VARCHAR(255)        DEFAULT NULL
  COMMENT '请求URL',
  `ip`           VARCHAR(255)        DEFAULT NULL
  COMMENT 'ip',
  `sql_text`     TEXT COMMENT '执行sql',
  `created_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gar_log_operation
-- ----------------------------
DROP TABLE IF EXISTS `gar_log_operation`;
CREATE TABLE `gar_log_operation` (
  `id`        BIGINT(20) NOT NULL,
  `url`       VARCHAR(200) DEFAULT NULL
  COMMENT '请求URL',
  `method`    VARCHAR(50)  DEFAULT NULL
  COMMENT '请求方法',
  `operation` VARCHAR(100) DEFAULT NULL
  COMMENT '操作',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of gar_log_operation
-- ----------------------------
INSERT INTO `gar_log_operation` VALUES ('1', '/login', 'POST', '用户登录');
INSERT INTO `gar_log_operation` VALUES ('2', '/userRoles', 'GET', '查询用户权限列表');
INSERT INTO `gar_log_operation` VALUES ('3', '/userList', 'GET', '查询用户名列表');
INSERT INTO `gar_log_operation` VALUES ('4', '/userRole', 'POST', '新增用户权限');
INSERT INTO `gar_log_operation` VALUES ('5', '/userRole', 'PUT', '更新用户权限');
INSERT INTO `gar_log_operation` VALUES ('6', '/userRole', 'DELETE', '删除用户权限');
INSERT INTO `gar_log_operation` VALUES ('7', '/distinctUserList', 'GET', '查询没有权限的用户');
INSERT INTO `gar_log_operation` VALUES ('8', '/users', 'GET', '查询所有用户列表');
INSERT INTO `gar_log_operation` VALUES ('9', '/password', 'POST', '修改用户密码');
INSERT INTO `gar_log_operation` VALUES ('10', '/user', 'POST', '新增用户');
INSERT INTO `gar_log_operation` VALUES ('12', '/user', 'PUT', '修改用户信息');
INSERT INTO `gar_log_operation` VALUES ('13', '/user', 'DELETE', '删除用户');
INSERT INTO `gar_log_operation` VALUES ('14', '/logs', 'GET', '查询系统日志');
INSERT INTO `gar_log_operation` VALUES ('15', '/user/', 'GET', '查询用户信息');
INSERT INTO `gar_log_operation` VALUES ('16', '/roleIds/', 'GET', '通过用户ID查询该用户的权限ID');
INSERT INTO `gar_log_operation` VALUES ('17', '/kaptcha', 'GET', '获取验证码');
INSERT INTO `gar_log_operation` VALUES ('18', '/roleUsers/', 'GET', '通过角色ID获取该角色的用户ID');
INSERT INTO `gar_log_operation` VALUES ('19', '/usersByRole', 'GET', '通过权限ID更新该权限的用户');
INSERT INTO `gar_log_operation` VALUES ('20', '/menu', 'GET', '查询系统菜单');
INSERT INTO `gar_log_operation` VALUES ('21', '/token/userInfo', 'GET', '通过token查询用户信息');


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gar_menu
-- ----------------------------
DROP TABLE IF EXISTS `gar_menu`;
CREATE TABLE `gar_menu` (
  `menu_id`   BIGINT(20) NOT NULL,
  `parent_id` BIGINT(20)   DEFAULT NULL
  COMMENT '父菜单ID，一级菜单为0',
  `name`      VARCHAR(50)  DEFAULT NULL
  COMMENT '菜单名称',
  `url`       VARCHAR(200) DEFAULT NULL
  COMMENT '菜单URL',
  `perms`     VARCHAR(500) DEFAULT NULL
  COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type`      INT(11)      DEFAULT NULL
  COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon`      VARCHAR(50)  DEFAULT NULL
  COMMENT '菜单图标',
  `order_num` INT(11)      DEFAULT NULL
  COMMENT '排序',
  PRIMARY KEY (`menu_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of gar_menu
-- ----------------------------
INSERT INTO `gar_menu` VALUES ('1', '0', '系统管理', NULL, NULL, '0', 'fa fa-cog', '0');
INSERT INTO `gar_menu` VALUES ('2', '1', '用户管理', 'modules/user.html', NULL, '1', 'fa fa-user', '1');
INSERT INTO `gar_menu` VALUES ('3', '1', '权限管理', 'modules/authority.html', NULL, '1', 'fa fa-th-list', '4');
INSERT INTO `gar_menu` VALUES ('4', '1', '系统日志', 'modules/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gar_roles
-- ----------------------------
DROP TABLE IF EXISTS `gar_roles`;
CREATE TABLE `gar_roles` (
  `role_id`     BIGINT(20) NOT NULL,
  `name`        VARCHAR(100)        DEFAULT NULL
  COMMENT '角色名称',
  `remark`      VARCHAR(100)        DEFAULT NULL
  COMMENT '备注',
  `create_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of gar_roles
-- ----------------------------
INSERT INTO `gar_roles` VALUES ('1', '网络指标分析', '拥有网络指标分析模块所有权限', '2017-08-25 15:52:55');
INSERT INTO `gar_roles` VALUES ('2', '网络故障分析', '拥有网络故障分析模块所有权限', '2017-08-25 15:53:50');
INSERT INTO `gar_roles` VALUES ('3', '业务指标分析', '拥有业务指标分析模块所有权限', '2017-08-25 15:54:19');
INSERT INTO `gar_roles` VALUES ('4', '业务故障分析', '拥有业务故障分析模块所有权限', '2017-08-25 15:54:59');
INSERT INTO `gar_roles` VALUES ('5', '主动告警', '拥有主动告警模块所有权限', '2017-08-25 15:55:56');
INSERT INTO `gar_roles` VALUES ('6', '短信告警', '拥有短信告警模块所有权限', '2017-08-25 15:58:56');
INSERT INTO `gar_roles` VALUES ('7', '用户信令回溯', '拥有用户信令回溯模块所有权限', '2017-08-25 16:01:03');
INSERT INTO `gar_roles` VALUES ('8', '实时监控', '拥有实时监控模块所有权限', '2017-08-31 16:43:39');
INSERT INTO `gar_roles` VALUES ('9', '重点指标分析', '拥有重点指标分析模块所有权限', '2017-08-31 16:44:02');
INSERT INTO `gar_roles` VALUES ('10', '弱覆盖区域Gis分析', '拥有弱覆盖区域Gis分析模块的所有权限', '2017-09-05 10:53:40');
INSERT INTO `gar_roles` VALUES ('11', 'API推送', '拥有API推送模块的所有权限', '2017-09-29 20:41:20');
INSERT INTO `gar_roles` VALUES ('12', '质差推送', '拥有质差推送模块所有权限', '2017-10-12 15:42:44');
INSERT INTO `gar_roles` VALUES ('13', 'Volte网络指标分析', '拥有Volte网络指标分析模块所有权限', '2017-11-09 18:48:16');
INSERT INTO `gar_roles` VALUES ('14', 'Volte网络故障分析', '拥有Volte网络故障分析模块所有权限', '2017-11-09 18:48:41');


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gar_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `gar_user_roles`;
CREATE TABLE `gar_user_roles` (
  `user_id` BIGINT(20) DEFAULT NULL
  COMMENT '用户ID',
  `role_id` BIGINT(20) DEFAULT NULL
  COMMENT '角色ID'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gar_user_token
-- ----------------------------
DROP TABLE IF EXISTS `gar_user_token`;
CREATE TABLE `gar_user_token` (
  `user_id`     BIGINT(20) NOT NULL,
  `token`       VARCHAR(100)        DEFAULT NULL
  COMMENT 'token',
  `update_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '更新时间',
  `expire_time` TIMESTAMP  NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '过期时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of gar_user_token
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gar_users
-- ----------------------------
DROP TABLE IF EXISTS `gar_users`;
CREATE TABLE `gar_users` (
  `user_id`     BIGINT(20)  NOT NULL,
  `username`    VARCHAR(50) NOT NULL
  COMMENT '用户名',
  `password`    VARCHAR(100)         DEFAULT NULL
  COMMENT '密码',
  `salt`        VARCHAR(20)          DEFAULT NULL
  COMMENT '盐',
  `email`       VARCHAR(100)         DEFAULT NULL
  COMMENT '邮箱',
  `mobile`      VARCHAR(100)         DEFAULT NULL
  COMMENT '手机号',
  `status`      INT(11)              DEFAULT NULL
  COMMENT '状态  0：禁用   1：正常',
  `create_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `admin`       INT(11)              DEFAULT '0'
  COMMENT '是否管理员 ，0：不是，1：是',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of gar_users
-- ----------------------------
INSERT INTO `gar_users` VALUES
  ('1', 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e',
   'admin@richstonedt.com', '020-38838993', '1', '2016-11-11 11:11:11', '2');

-- ----------------------------------------
-- Table structure for gar_user_synchronize
-- ----------------------------------------
DROP TABLE IF EXISTS `gar_user_synchronize`;
CREATE TABLE `gar_user_synchronize` (
  `id` bigint(20) NOT NULL,
  `updated_time` datetime DEFAULT NULL COMMENT '上次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录上次用户同步时间';

-- ----------------------------
-- Records of gar_user_synchronize
-- ----------------------------
INSERT INTO `gar_user_synchronize` VALUES ('1', null);