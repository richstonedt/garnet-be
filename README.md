
# 分支说明：
> 1. 本分支来自 garnet-core-be-fe v2.1.0 develop 分支；
> 2. 本分支分离了前端代码，只保留后端代码。
> 
> by Jaffray, 2019/06/07 1:06AM

---

**项目说明** 

- **Garnet安全管理模块为独立的安全管理模块，负责管理系统用户、角色、权限以及用户日志等的单点登录系统。系统包括了前后端，其中后端使用了Springboot搭建，前端基于Vue2框架进行开发。**

**具有如下特点** 
- 实现前后端分离，通过token进行数据交互，前端再也不用关注后端技术
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 引入API模板，根据token作为登录令牌，极大的方便了APP接口开发
- 引入Hibernate Validator校验框架，轻松实现后端校验
- 引入swagger文档支持，方便编写API接口文档
- 引入路由机制，刷新页面会停留在当前页

**数据权限设计思想** 
- 管理员管理、角色管理、部门管理，可操作本部门及子部门数据
- 菜单管理、定时任务、参数管理、系统日志，没有数据权限
- 业务功能，按照用户数据权限，查询、操作数据
- 没有本部门数据权限，也能查询、操作本人数据

**项目结构** 
```
garnet
├─SQL  项目SQL语句
│
├─common 公共模块
│  ├─aspect 系统日志
│  ├─exception 异常处理
│  ├─validator 后台校验
│  └─xss XSS过滤
│ 
├─config 配置信息
│ 
├─modules 功能模块
│  ├─api API接口模块(APP调用)
│  ├─job 定时任务模块
│  └─sys 权限模块
│ 
├─Application 项目启动类
│  
├─resources
   ├─mapper_bk SQL对应的XML文件
   ├─static 第三方库、插件等静态资源
   └─views  项目静态页面
```

**技术选型：** 
- 核心框架：Spring Boot 1.5
- 安全框架：Apache Shiro 1.3
- 视图框架：Spring MVC 4.3
- 持久层框架：MyBatis 3.3
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.0
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x

 **本地部署**
- 下载源码
- 使用Postgres，创建数据库garnet，数据库编码为UTF-8
- 执行doc/db_postgre.sql文件，初始化数据
- 修改application-dev.yml的数据库账号和密码
- 运行Application.java，则可启动项目
- 项目访问路径：http://localhost:8082/garnet
- 账号密码：admin/admin
- Swagger路径：http://localhost:8082/garnet/swagger/index.html




 **前端权限对接(前端框架均以Angular为例)**

- 登录页面

  - 验证码

  ```html
    <div class="input-group">
       <input class="form-control" style="width: 200px" ng-model="captcha" placeholder="验证码">&nbsp;&nbsp;&nbsp;
          <img alt="如果看不清楚，请单击图片刷新！" class="pointer" ng-src="{{src}}" ng-click="refreshCode()">&nbsp;&nbsp;&nbsp;
          <a style="margin-top: 16px" ng-click="refreshCode()">点击刷新</a>
     </div>
  ```

  <br>

  - 登录JS代码 <br>

  ```javascript
      $scope.error = false;

      /** 获取当前时间 */
      let nowTime = $.now();

      /** 默认加载验证码 */
      $scope.src = garnetPath + 'kaptcha?nowTime=' + nowTime;

      /** 登录图片 */
      $scope.isLogging = false;

      /** 登录接口 */
      $scope.gar_login = () => {
          if (checkUserInfo()) {
              $scope.isLogging = true;
              HttpRequestService.post(garnetPath + 'sys/login?loginFrom=gempile', {}, {
                  captcha: $scope.captcha,
                  nowTime: nowTime,
                  username: $scope.username,
                  password: $scope.password,
              }, response => {
                  if (response.code == 0) {//登录成功
                      localStorage.setItem('garnetToken', response.garnetToken);
                      localStorage.setItem('gempileToken', response.gempileToken);
                      $scope.isLogging = false;
                      $state.go('home');
                  } else {
                      $scope.error = true;
                      $scope.errorMsg = response.msg;
                      $scope.isLogging = false;
                  }
              });
          }
      };

      /** 刷新验证码 */
      $scope.refreshCode = () => {
          nowTime = $.now();
          $scope.src = garnetPath + 'kaptcha?nowTime=' + nowTime;
      };
  ```

  <br>

- 登录成功<br>
  用户登录成功之后会将两个带有用户信息的`token`存放在 `localStorage` 中，其中主要用到的是 `gempileToken`，该`token`经过[JWT](http://www.jianshu.com/p/576dbf44b2ae)加密，里面包含了：用户名，用户ID，是否管理员，拥有角色，`token`过期时间 这些信息。<br>

```java
return JWT.create()
      .withClaim("uid", garUser.getUserId().toString()) // 用户ID
      .withClaim("una", garUser.getUsername()) // 用户名
      .withClaim("uad", garUser.getAdmin())  // 是否管理员
      .withClaim("rol", roleIds)  // 拥有权限
      .withExpiresAt(new Date(System.currentTimeMillis() + (long) 60 * 60 * 1000 * 3)) //token过期时间 
      .sign(algorithm);
```

<br>

- 前端处理返回信息<br>
  - 为了安全性，用户数据都经过[JWT](http://www.jianshu.com/p/576dbf44b2ae)加密。
  - 下面以解析`role`为例，解析其他值，只需要将 `rol` 换成上面的key即可:<br>

```javascript
   let token = localStorage.getItem('gempileToken');
   let role;
   if (!token) {
       return false;
    }
    try {
       role = jwt.decode(token, 'secret').rol;
    } catch (error) {
       return false;
    }
```

&nbsp;&nbsp;&nbsp;&nbsp;注意：<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;①、其中`uid` 用于该用户唯一标识，`una` 用户名，0 表示普通用户，1 表示管理员，`rol` : 该用户拥有的权限，以”,”隔开。<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;②、`token`中的过期时间表示该`token`的有效时间（设置为3小时），如果过了这个时间，该`token` 就会过期，即 调用 `jwt.decode()` 就会报错，所以任何地方调用该方法均需`try,catch`；<br><br>

- 权限控制

  - 如果 `uad = 1` 表示该用户为管理员，可通过按钮进入权限系统；反之，则只能修改自己的密码。
  - 显示 `rol`有的模块。
  - 这些都可以通过在html 中通过 `ng-if` 及双向数据绑定 控制。<br>

  ```javascript
  const allModules = {
      1: 'networkIndicators',
      2: 'networkFailure',
      3: 'businessIndicators',
      4: 'businessFailure',
      5: 'autoAlert',
      6: 'smsAlert',
      8: 'realTimeMonitoring',
      9: 'keyIndicators',
      7: 'subscriberXdrRetrieval',
      10: 'poorAreaGis',
      11:'apiPush',
      12:'qualityDiffer',
      13:'volteNetworkIndicators',
      14:'volteNetworkFailure',
  };

      /**
       * logged in
       * 判断用户是否登录 
       *
       * @returns
       */
      loggedIn() {
          let token = localStorage.getItem('gempileToken');
          if (token) {
              try {
                  jwt.decode(token, 'secret');
                  return true;
              } catch (error) {
                  return false;
              }
          }
          return false;
      }

      /**
       * log out
       * 用户注销
       *
       * @param {Function} cb
       */
      logout(cb) {
          localStorage.removeItem('gempileToken');
          localStorage.removeItem('garnetToken');
          if (cb) {
              cb();
          }
      },
      /**
       * get Allow Modules
       *
       * @param roleIds
       */
      getAllowModules(roleIds){
          let roleIdArray = roleIds.split(',');
          let ownModules = ['home', 'login', 'homePage']; // 固有权限
          for(let i = 0;i < roleIdArray.length;i++){
              ownModules.push(allModules[roleIdArray[i]]);
          }
          return ownModules;
      }

      /**
       * check authentication
       * 权限控制 
       *
       * @param stateRef
       */
      doAuthentication(stateRef) {   
          /** 获取当前用户的角色组 */
          let token = localStorage.getItem('gempileToken');
          let role;
          if (!token) {
              return false;
          }
          try {
              role = jwt.decode(token, 'secret').rol;
          } catch (error) {
              return false;
          }

          /** 判断是否为当前角色可访问的模块 */
          const stateRefGroup = this.getAllowModules(role);
          if (!stateRefGroup.includes(stateRef)) {
              console.warn(stateRef + '：用户没有访问该模块的权限！');
              return false;
          }

          return true;
      }

  ```

   <br>   

- 路由控制<br>
  为防止用户通过url直接访问没有权限的模块，应在前端路由时加入权限判断。<br>

```javascript
app.config(($stateProvider, $urlRouterProvider, $compileProvider, $controllerProvider) => {
    $urlRouterProvider.otherwise('/home/homePage');
    app.registerCtrl = $controllerProvider.register;
    // set ui router
    StateConfig.states.map(state => {
        $stateProvider.state(state.stateRef, state.stateObj)
    })
}).run(['$rootScope', '$location', '$state', '$sce',($rootScope, $location, $state) => {
    if (!auth.loggedIn()) {
        $state.go('login');
    }
    $rootScope.$on('$locationChangeStart', () => {
        if (!auth.loggedIn()) {
            $state.go('login');
        }
    });
    $rootScope.$on('$stateChangeStart', (event, toState) => {
        let targetPath = toState.url;
        if (auth.loggedIn()) {
            if (targetPath.includes('/') && (targetPath == '/home' || targetPath == '/login')) {
                event.preventDefault();
                $state.go('homePage');
            }
            if (!auth.doAuthentication(toState.name)) {
                swal('', '用户没有访问该模块的权限！', 'warning');
                event.preventDefault();
            }
        } else if (targetPath != '/login') {
            $state.go('login');
        }
    });
}])
```



### 第三方应用接入系统

需要访问接口`/garnet/api/v1.0/users/refreshtoken`，请求方式为`POST`，请求参数如下所示

```json
{
  "appCode": "szcst",
  "refreshToken":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiIsImFwcENvZGUiOiJzemNzdCIsInVzZXJOYW1lIjoiYWRtaW4ifQ.IaL_S-Wv64mDTVKPziR3AveBGstNa3mAw7hXC1RKPcU#szcst#admin#1546416446732#refreshToken",
 "tenantIdList": [
    5440869955148
  ],
  "userName": "admin"
}

```

下面是接口返回的响应参数：

```json
{
  "message": "token刷新成功",
  "user": null,
  "loginStatus": "success",
  "code": 201,
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiIsImFwcENvZGUiOiJzemNzdCIsInVzZXJOYW1lIjoiYWRtaW4ifQ.IaL_S-Wv64mDTVKPziR3AveBGstNa3mAw7hXC1RKPcU#szcst#admin#1546416637398#accessToken",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiIsImFwcENvZGUiOiJzemNzdCIsInVzZXJOYW1lIjoiYWRtaW4ifQ.IaL_S-Wv64mDTVKPziR3AveBGstNa3mAw7hXC1RKPcU#szcst#admin#1546416637398#refreshToken",
  "refreshTokenResourceList": [],
  "resourceDynamicPropertyList": [],
  "userTenantNameAndIdMap": null,
  "typeResourceListMap": {
    "szcst": [
      {
        "id": 5440924537787,
        "applicationId": 5440872844526,
        "path": "/report/view",
        "action": "edit",
        "name": "ICT-报告模块-查看权限",
        "createdTime": 1544092453777,
        "modifiedTime": 1544092587280,
        "type": "shenzhen",
        "tenantId": 5440869955148,
        "varchar00": "",
        "varchar01": "",
        "varchar02": "",
        "varchar03": "",
        "varchar04": "",
        "varchar05": "",
        "varchar06": "",
        "varchar07": "",
        "varchar08": "",
        "varchar09": "",
        "varchar10": "",
        "varchar11": "",
        "varchar12": "",
        "varchar13": "",
        "varchar14": "",
        "varchar15": "",
        "varchar16": "",
        "varchar17": "",
        "varchar18": "",
        "varchar19": "",
        "int01": null,
        "int02": null,
        "int03": null,
        "int04": null,
        "int05": null,
        "boolean01": false,
        "boolean02": false,
        "boolean03": false,
        "boolean04": false,
        "updatedByUserName": "admin"
      },
      {
        "id": 5440925067947,
        "applicationId": 5440872844526,
        "path": "/sms/view",
        "action": "edit",
        "name": "ICT-短信模块-查看权限",
        "createdTime": 1544092506796,
        "modifiedTime": 1544092596033,
        "type": "shenzhen",
        "tenantId": 5440869955148,
        "varchar00": "",
        "varchar01": "",
        "varchar02": "",
        "varchar03": "",
        "varchar04": "",
        "varchar05": "",
        "varchar06": "",
        "varchar07": "",
        "varchar08": "",
        "varchar09": "",
        "varchar10": "",
        "varchar11": "",
        "varchar12": "",
        "varchar13": "",
        "varchar14": "",
        "varchar15": "",
        "varchar16": "",
        "varchar17": "",
        "varchar18": "",
        "varchar19": "",
        "int01": null,
        "int02": null,
        "int03": null,
        "int04": null,
        "int05": null,
        "boolean01": false,
        "boolean02": false,
        "boolean03": false,
        "boolean04": false,
        "updatedByUserName": "admin"
      }
    ]
  }
}

```

`typeResourceListMap`代表着返回的资源列表，其中我们在前端以`name`判断用户是否有这个权限，当我们遍历资源列表，发现没有某个功能的名称和`name`不对应，那么就代表没有权限。`path`代表改资源访问的后台接口的`url`。