##本工具包所包含的功能
1. 定长文件解析到excel


### 定长文件解析器说明
#### 功能说明：
    将定长报文按指定的长度截取字段值到excel中，解决肉眼看定长文件效率低的问题

#### 注意事项：
    1. 只支持GBK
    2. 只支持简单格式的定长文件，不支持有头尾等复杂格式的文件
    3. 只按字节截取，而非字符

#### 使用说明：
    1. 下载压缩包
    2. 解压压缩包
    3. 配置file_formate.properties文件，可以配置多个文件
       key为文件名，value为字段截取范围和字段名
       如：test.txt=1-3:id;4-10:name;11-20:memo
       代表有一个test.txt的文件，1-3个字节为id，4-10个字节为name，11-20个字节为memo
    4. 把要转换的定长文件放到解压后的目录中
    5. 执行（操作系统需要安装jdk）
       命令模式执行 java -jar tool-1.0-SNAPSHOT.jar test.txt其中test.txt为要转换的文件名
    6. 文件生成，如果已经生成过会删除重新生成
#### 打包说明：
    maven clean package之后生成一个tool-1.0-SNAPSHOT.jar
    把target/classes/下的lib目录拷贝出来
    file_format.properties也拷贝出来
    这三个放到一个目录下，打个包。
    