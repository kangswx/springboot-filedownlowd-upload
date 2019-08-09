1. go语言的第一行必须声明包

2. 入口的go语言代码（包含main函数的代码文件）,的包必须是main，否则运行go程序会报错 go run: cannot run non-main package

3. 左花括号"{"不能单独占一行

4. 导入go库

   ```go
   package main
   ```

5. 

6. helloworld

   ```go
   package main
   
   import "fmt"
   
   func main(){
   	fmt.Printf("hello world\n");
   }
   ```

7. 测试运行go程序

   ```go
   go run HelloWorld.go
   ```

8. 编译go语言

   ```go
   go build HelloWorld.go
   ```

   编译后的文件为：HelloWorld.exe

9. 执行编译完成的go程序

   ```go
   HelloWorld
   ```

10. 

