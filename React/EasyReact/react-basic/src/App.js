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
