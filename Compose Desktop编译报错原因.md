Compose Desktop 工程中
当java代码和kotlin代码放在一起（src/main/kotlin/package）

会出现以下报错信息：
[Exception in thread "main" java.lang.NoClassDefFoundError]

原因是：
java和kotlin放在一起，虽然未出现编译报错，但是运行时会报错，
根本原因：系统无法知道使用哪种类加载器

故：代码需要分类放置
kotlin代码：src/main/kotlin/package
java代码：src/main/java/package
