package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)/*表示注解类应该在什么位置，对哪一块的数据有效
1.constructor:用于描述构造器
2.field:用于描述域
3.local_variable:用于描述局部变量
4.method:用于描述方法
5.package:用于描述包
6.parameter:用于描述参数
7.type:用于描述类、接口、或者enum声明
*/
@Retention(RetentionPolicy.RUNTIME)//注解的生命周期
public @interface LoginRequired {

}
