package com.eks.thread.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.InputStream;

@Data//注解在类上;提供类所有非static且非final属性的get和set方法,final属性只提供get方法,此外还提供了equals、canEqual、hashCode、toString 方法
@EqualsAndHashCode(callSuper = false)//是否调用父类的equals和hachCode方法
@Accessors(chain = true)//chain=boolean值，默认false。如果设置为true，setter返回的是此对象，方便链式调用方法
@NoArgsConstructor//无参构造器
@AllArgsConstructor//全参构造器
public class SocketInputThread extends Thread{
    private InputStream inputStream = null;
}
