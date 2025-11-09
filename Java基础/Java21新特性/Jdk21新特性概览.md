# Java 21 新特性概览

## 概述

Java 平台自 JDK 21 起进入新的长期支持（LTS）阶段。根据 OpenJDK 公告，JDK 21 于 2023 年 9 月 19 日发布并包含 15 项特性[openjdk.org](https://openjdk.org/projects/jdk/21/#:~:text=Features)。长时间支持版本意味着厂商承诺提供更长时间的安全补丁和更新[infoworld.com](https://www.infoworld.com/article/2338097/jdk-21-the-new-features-in-java-21.html#:~:text=Java Development Kit  ,collector%2C was dropped in June)。JDK 21 继续推进项目 Loom、Amber 和 Panama，为开发者带来新的语言结构、并发模型以及库改进。

## 语言特性

### 字符串模板（JEP 430，预览）

字符串模板通过引入“模板处理器”将文字文本与内嵌表达式耦合，让动态字符串构建更加简单、安全。该特性允许开发者编写类似 `STR."Hello, \{name}!"` 的表达式，由模板处理器负责格式化和验证[openjdk.org](https://openjdk.org/jeps/430#:~:text=Summary)。目标是简化运行时生成字符串的方式、提升可读性，并在生成 SQL、JSON 等文本时提高安全性[openjdk.org](https://openjdk.org/jeps/430#:~:text=,values computed at run time)。作为预览特性，需要通过编译选项启用。

### 记录模式（JEP 440，正式）

记录模式扩展了模式匹配，使程序可以在声明位置分解记录类型。例如 `case Point(var x, var y) ->` 可直接解构 `Point` 对象的字段[openjdk.org](https://openjdk.org/jeps/440#:~:text=Enhance the Java programming language,of data navigation and processing)。记录模式与类型模式可以嵌套，从而以声明式方式访问复杂数据结构[openjdk.org](https://openjdk.org/jeps/440#:~:text=Record patterns were proposed as,upon continued experience and feedback)。该特性在 JDK 21 中正式确定（结束预览）。

### switch 的模式匹配（JEP 441，正式）

模式匹配为 `switch` 表达式和语句引入了根据对象结构执行分支的能力。可以在 `case` 标签中使用类型模式或守护条件，使 `switch` 能够同时检查类型与提取数据。例如：

```
java复制编辑switch (obj) {
    case String s -> System.out.println("字符串长度=" + s.length());
    case Integer i -> System.out.println("平方=" + i*i);
    default -> System.out.println("未知类型");
}
```

JEP 描述允许 `switch` 匹配值与模式并执行相应动作[openjdk.org](https://openjdk.org/jeps/441#:~:text=Enhance the Java programming language,be expressed concisely and safely)。其目标是提高表达力与安全性，同时确保旧的 `switch` 行为不被破坏[openjdk.org](https://openjdk.org/jeps/441#:~:text=Goals)。

### 未命名模式与未命名变量（JEP 443，预览）

该特性引入下划线 `_` 作为占位符，用于模式匹配时忽略某些组件，或声明但不使用的局部变量。例如在分解 `Point` 时，可以写 `case Point(_, int y) ->` 来忽略第一个字段。这使模式匹配更简洁，并允许显式标记“未使用的变量”[openjdk.org](https://openjdk.org/jeps/443#:~:text=Enhance the Java language with,is a preview language feature)。此外，引入未命名模式后，某些记录模式可以省略嵌套模式以提升可读性[openjdk.org](https://openjdk.org/jeps/443#:~:text=,by eliding unnecessary nested patterns)。

### 未命名类与实例 `main` 方法（JEP 445，预览）

为了让初学者更容易上手，JEP 445 允许编写不带显式类声明的简单程序，并支持实例方法作为应用程序的入口。例如可以在单个 Java 文件中直接编写 `void main()` 而不显式声明类[openjdk.org](https://openjdk.org/jeps/445#:~:text=Summary)。此举旨在降低编写“Hello World”程序的礼仪成本，为学习者提供顺滑的过渡[openjdk.org](https://openjdk.org/jeps/445#:~:text=* Offer a smooth on,concepts in a gradual manner)。

## 并发与线程

### 虚拟线程（JEP 444，正式）

虚拟线程是可由用户代码大量创建的轻量线程。JDK 21 正式将其纳入平台，结束了先前两个版本的预览阶段[openjdk.org](https://openjdk.org/jeps/444#:~:text=Introduce virtual threads to the,throughput concurrent applications)。虚拟线程支持所有标准的线程操作，包括线程局部变量，并受监视系统支持[openjdk.org](https://openjdk.org/jeps/444#:~:text=,code to use virtual threads)。它们使得以“每个任务一线程”的模型编写高吞吐量服务器成为可能，同时降低内存开销和调度成本。

### 结构化并发（JEP 453，预览）

结构化并发提供一种 API，使一组相关的任务作为单元一起执行和管理。当多个子任务在不同线程上运行时，开发者可以像管理单个任务一样处理它们，从而简化错误处理和取消逻辑，并改善可靠性与可观测性[openjdk.org](https://openjdk.org/jeps/453#:~:text=Summary)。该 API 与虚拟线程结合，可以简化并发编程模型。

### 作用域值（JEP 446，预览）

作用域值是一种不可变的数据，它们可以在调用栈范围内显式共享。相比线程局部变量，作用域值在使用大量虚拟线程时更加安全且效率更高。调用者可以将值绑定到作用域中并由被调用者访问，而无需显式传递[openjdk.org](https://openjdk.org/jeps/446#:~:text=Summary)。

## 库与运行时改进

### 顺序集合（JEP 431）

顺序集合为具有确定“遇见顺序”的集合（如 `List`、`Deque`、有序 `Set` 等）提供统一的接口 `SequencedCollection`。它定义了 `first()`、`last()`、`reversed()` 等操作，并允许在两端添加或删除元素。这旨在弥补标准集合框架在迭代方向和双端操作上的不一致[openjdk.org](https://openjdk.org/jeps/431#:~:text=Introduce new interfaces to represent,its elements in reverse order)。JEP 阐述了缺乏统一 API 给开发者带来的困惑，并提出在集合层次中引入新的接口和默认方法[openjdk.org](https://openjdk.org/jeps/431#:~:text=Java’s collections framework lacks a,source of problems and complaints)。

### 向量 API（JEP 448，第六次孵化）

向量 API 为向量计算提供抽象，使编写的运算可在支持的 CPU 上编译为高效的向量指令。目标包括易用、平台无关的 API，并提供可靠的性能和优雅降级[openjdk.org](https://openjdk.org/jeps/448#:~:text=Summary)[openjdk.org](https://openjdk.org/jeps/448#:~:text=,hardware supporting different vector sizes)。第六次孵化版本继续改进性能和稳定性。

### 外部函数与内存 API（JEP 442，第三次预览）

该 API 允许 Java 程序调用非 JVM 代码并访问非托管内存，而无需使用 JNI。第三次预览引入了 “Arena” 管理内存生命周期、增强了 `MemoryLayout` 的路径表达、提供后备链接器并移除了 `VaList`[openjdk.org](https://openjdk.org/jeps/442#:~:text=Introduce an API by which,This is a preview API)[openjdk.org](https://openjdk.org/jeps/442#:~:text=,class)。这些改进旨在简化与本机库的交互并提升安全性和性能。

### 分代 ZGC 垃圾收集器（JEP 439）

Z 垃圾收集器以低停顿著称，但在前版本中采用单一代策略。分代 ZGC 将堆划分为年轻代和老年代，年轻对象更频繁地回收，老对象较少移动。此设计减少了分配停顿、降低内存占用和 CPU 开销，同时保持低停顿和良好扩展性[openjdk.org](https://openjdk.org/jeps/439#:~:text=Improve application performance by extending,die young — more frequently)[openjdk.org](https://openjdk.org/jeps/439#:~:text=Applications running with Generational ZGC,should enjoy)。用户无需额外配置即可受益。

### 密钥封装机制 API（JEP 452）

密钥封装机制（KEM）是一种利用公钥加密保护对称密钥的技术，用于 TLS 和 HPKE 等协议。JEP 452 引入了支持 RSA‑KEM、ECIES 以及 NIST 候选后量子算法等 KEM 的 API[openjdk.org](https://openjdk.org/jeps/452#:~:text=Summary)。新的 API 允许提供者在 Java 或本机代码中实现 KEM，并提供了 Diffie–Hellman KEM 的实现，增强了安全库的可扩展性。

## 平台变更与废弃项

### 废弃 Windows 32‑bit x86 端口（JEP 449）

JDK 21 宣布废弃 Windows 32 位 x86 端口，并计划在未来版本中移除。构建系统会显示错误（除非用户明确禁用），文档也标记相应状态[openjdk.org](https://openjdk.org/jeps/449#:~:text=Summary)。该决策部分原因是 Windows 10 32 位版本将在 2025 年 10 月结束支持，同时该端口在虚拟线程等功能上存在性能问题[openjdk.org](https://openjdk.org/jeps/449#:~:text=,of Life in October 2025)。

### 准备禁止动态加载代理（JEP 451）

为了加强“默认完整性”（Integrity By Default），JDK 21 当检测到运行时动态加载代理时会发出警告，为未来版本默认禁止此操作做准备[openjdk.org](https://openjdk.org/jeps/451#:~:text=Issue warnings when agents are,be issued in any release)。静态在启动时加载的服务可继续使用而不会触发警告。

## 总结

JDK 21 作为长期支持版本，在语言层面提供了字符串模板、记录模式、switch 模式匹配、未命名模式与未命名类等创新，为初学者和资深开发者提供更简洁的表达方式；在并发方面引入了虚拟线程、结构化并发和作用域值，显著改善高并发编程体验；库与运行时方面更新了顺序集合、向量 API、外部函数与内存 API、分代 ZGC 以及密钥封装 API，使 Java 在性能、互操作性和安全性上更强大。与此同时，JDK 21 也开始废弃不再维护的端口并规范代理机制，为未来的版本清理技术负担。
