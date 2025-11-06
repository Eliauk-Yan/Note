// 项目的入口 从这里开始运行

// React 不要的两个核心包
import React from 'react';
import ReactDOM from 'react-dom/client';

// 导入项目的根组件
import App from './App';

// 把App 根组件渲染到ID为root的DOM节点上
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <App/>
);
