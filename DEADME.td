1.定义配置 RedisListenerConfig 实现监听 Redis key 过期时间 RedisListenerConfig

2.策略模式 HandleProcessor

3.Redis+拦截器+自定义Annotation实现接口自动幂等 AutoIdempotentInterceptor

4.点赞功能 BaseController

5.fastDFS文件上传下载功能

6.统一异常处理 GlobalExceptionHandler

7.shrio框架 MyShiroRealm

8.mybatis-plus

9.接口访问日志记录 LogsAllInterceptor

10.统一返回对象 PageJSONResult

11.hcs-core包 短信邮件发送 第三方登录 文件上传下载

12.随机生成4位数 TinyUrlUtils

13.路由 RouterFactoryBean

14.redis操作类 RedisOperate

15.word导出、execl导出、打压缩包工具类

16.json与pojo转换 MapperUtils

17.短信发送 SmsNoticeService

18.mybatiscodehelper

19.访问其他系统 HttpAPIService

20.mysql读写分离 DataSourceConfig

21.WebSocket实现服务端向客户端推送消息 WebSocket

22.动态代理 MapperProxyFactory

23.集群环境下通过redis锁确保定时任务只有一台机器上执行 CacheLockAspect

24.定时清理 ExpiredCleanService

25.敏感字段加密解密 DecryptInterceptor mybatis拦截器

26.数据库表结构文档生成工具 DocumentGenerationUtils

27.线程池创建 VisiableThreadPoolTaskExecutor

28.shiro免登录 CybSimpleCredentialsMatcher

29.自定义监听器 MyEventListener

30.第三方登录工具,里面有十几家对接平台 JustAuthController

31.文件上传下载功能 FileController

32.token的方式登录 TokenSessionManager

33.需要分页的时候参数处理 CommonQueryPageUtils

34.配置日期返回格式(时间戳的格式返回)
    jackson:
           serialization:
             write-dates-as-timestamps: true

35.本地缓存caffeine CacheConfig
    cache:
        type: caffeine
        cache-names:
          - userCache
        caffeine:
          spec: maximumSize=1024,refreshAfterWrite=60s

