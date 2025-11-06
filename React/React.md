# React

**作者：YanShijie**

---

## 1. React 简介

**（1）React 是什么？**

​	React 由Meta公司研发，是一个用于构建Web和原生交互界面的库。

**（2）React 优势**

​	**相较于传统的基于DOM开发的优势：**组件化的开发模式、不错的性能

​	**相较于其他前端框架的优势：**丰富的生态、跨平台支持

**（3）市场情况**

​	全球最流行、大厂必备

---

## 2. React 开发环境搭建

### 2.1 `create-react-app`快速搭建开发环境

​	`create-react-app` 是一个快速创建 **React开发环境的工具**，底层由Webpack构建，**封装了配置细节**，开箱即用。

**执行命令：**

```cmd
npx create-react-app [项目名]
```

> 命令解释：
>
> - **npx：** Node.js 工具命令，查找并执行后续的命令包
> - **create-react-app：**核心包（固定写法），用于创建React项目

**项目启动命令：**

```cmd
npm run(可省略) start
```

**项目目录文件：**

```cmd
src
	App.js # 项目的根组件
	index.js # 整个项目的入口 从这里开始运行
```

---

## 3. JSX基础

### 3.1 概念和本质

​	**1. 概念：**JSX是JavaScript和XML（HTML）的缩写，表示在JS代码中编写HTML模板的结构，它是React中编写UI模板的方式。

​	**2. 优势：**HTML的声明式模板写法、JS可编程能力。

​	**3. 本质：**JXS并不是表中的JS语法，它式**JS语法扩展**，浏览器本身不能识别，它需要通过**解析工具做解析**之后才能在浏览器中运行。

![image-20251105200601622](images/image-20251105200601622.png)

> **Babel网址：**https://babeljs.io

### 3.2 高频场景

​	在JSX中可以通过 **大括号语法{}** 识别JavaScript中的表达式，比如常见的变量、函数调用、方法调用等等。

```jsx
// 项目的根组件
// App -> index.js -> public/index.html(root)

const count = 100

const getName = () => {
    return 'jack'
}

function App() {
    return (
        <div className="App">
            This is App!
            {/*{1. 使用引号传递字符串}*/}
            {'this is message'}
            {/*{2. 识别js变量}*/}
            {count}
            {/*3. 函数调用*/}
            {getName()}
            {/*4. 方法调用共*/}
            {new Date().getDate()}
            {/*5. 使用Js对象*/}
            <div style={{color: 'red'}}> this is red! </div>
        </div>
    );
}

export default App;
```

### 3.3 实现列表渲染

1. **语法：**在JSX中可以使用原生JS中的map方法便利渲染列表

```jsx
const list = [
    {id: '1', name: 'Vue'},
    {id: '2', name: 'React'},
    {id: '3', name: 'Angular'},
]

function App() {
    return (
        <div className="App">
            this is APP
            {/*渲染列表*/}
            {/*map 循环哪个结构 return结构*/}
            {/*注意事项：加上一个独一无二的key 字符串或者式number id*/}
            {/*key的作用：React框架内部使用的一个东西，用来提升列表的更新性能*/}
            <ul>
                {list.map(item => <li key={item.id}>{item.name}</li>)}
            </ul>
        </div>
    );
}

export default App;
```

### 3.4 基础条件渲染

1. **语法：**可以通过**逻辑与运算&&、三元表达式（？：）**实现基础条件渲染

```jsx
const isLogin = false

function App() {
    return (
        <div className="App">
            {/*逻辑与 &&*/}
            {isLogin && <span>this is span </span>}
            {/*三元运算*/}
            {isLogin ? <span>this is span </span> : <span>loading...</span>}
        </div>
    );
}

export default App;

```

### 3.5 复杂条件渲染

1. **解决方案：**自定义函数 + if 判断语句

```jsx
const articleType = 2

// 定义核心函数（根据文章类型返回不同的JSX模板）
const getArticleType = () => {
    if (articleType === 0) {
        return <div>我是无图文章</div>
    } else if (articleType === 1) {
        return <div>我是单图模式</div>
    } else {
        return <div>我是多图模式</div>
    }
}

function App() {
    return (
        <div className="App">
            {getArticleType()}
        </div>
    );
}

export default App;
```

---

## 4. React 基础事件绑定

1. **语法**：**on + 事件名称 = {事件处理程序}**，整体上遵循驼峰命名法。

```jsx
// 基础使用
// const handleClick = () => {
//     console.log('click');
// }

// 事件参数e
// const handleClick = (e) => {
//     console.log('button 被点击了！', e);
// }

// 传递自定义参数
// const handleClick = (name) => {
//     console.log('button 被点击了！', name);
// }

// 既要传递自定义参数，而且还要事件对象e
const handleClick = (name, e) => {
    console.log('button 被点击了！', name, e);
}
function App() {
    return (
        <div className="App">
            <button onClick={(e) => handleClick('jack', e)}>click me!</button>
        </div>
    );
}

export default App;
```

---

## 5. React组件基础使用

1. **概念：**一个组件就是用户界面的一部分，它可以由自己的逻辑和外观，组件之间**可以相互嵌套，也可以复用多次。**

<img src="images/image-20251105205025552.png" alt="image-20251105205025552" style="zoom:33%;" />

2. **语法：**在React中一个组件就是一个**首字母大写的函数**，内部存放了组件的逻辑和视图UI，渲染组件只需要把组件当成**标签书写即可**。

```js
// 1. 定义组件
const Button = () => {
    // 业务逻辑组件逻辑
    return <button>click me !</button>
}

function App() {
    return (
        <div className="App">
            {/*2. 使用组件（渲染组件）*/}
            {/*自闭和*/}
            <Button />
            {/*成对标签*/}
            <Button></Button>
        </div>
    );
}

export default App;
```

## 6. useState 基础使用

### 6.1 基础使用

1. **概念：**useState 是一个 React Hook 函数，它运行我们向组件添加一个**状态变量**，从而控制影响组件渲染结果。

2. **本质：**和普通Js变量不同的是，状态变量一旦发生变化组件的视图UI也会跟着变化（**数据驱动视图**）

3. **语法：**

![image-20251105210057409](images/image-20251105210057409.png)

4. **案例（计数器）**

```jsx
import {useState} from "react";

function App() {
    // 1.调用useState添加一个状态变量
    const [count, setCount] = useState(0)

    // 2. 点击事件回调
    const handleClick = () => {
        // 作用
        // 1：用传入的新值修改count
        // 2.重新使用新的count渲染UI
        setCount(count + 1)
    }
    return (
        <div className="App">
            <button onClick={handleClick}>{count}</button>
        </div>
    );
}

export default App;
```

### 6.2 修改状态规则

1. **状态不可变：**在React中，状态被认为是只读的，我们应该始终**替换它而不是修改它**，直接修改不能引发视图更新。

<img src="images/image-20251105210930587.png" alt="image-20251105210930587" style="zoom:50%;" />

2. **修改对象状态：**对于对象类型的状态变量，应该始终传给set方法一个**全新的对象**来进行修改。

<img src="images/image-20251105211236857.png" alt="image-20251105211236857" style="zoom:33%;" />

3. **示例代码：**

```jsx
import {useState} from "react";

function App() {
    // 1.调用useState添加一个状态变量
    const [count, setCount] = useState(0)

    // 2. 点击事件回调
    const handleClick = () => {
        setCount(count + 1);
    }

    // 修改对象状态
    const [form, setForm] = useState({name:'tom'})

    const changeForm = () => {
        // 错误写法
        // form.name = 'jack'
        // 正确写法
        setForm({
            ...form,
            name:'jack'
        })
    }
    return (
        <div className="App">
            <button onClick={handleClick}>{count}</button>
            <button onClick={changeForm}>{form.name}</button>
        </div>
    );
}

export default App;
```

