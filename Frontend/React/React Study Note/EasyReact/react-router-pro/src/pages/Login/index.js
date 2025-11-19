import {useNavigate} from 'react-router-dom';

const Login = () => {
    const navigate = useNavigate();
    return (
        <div>
            我是登录页
            <button onClick={() => navigate('/article')}>跳转文章</button>
            <button onClick={() => navigate('/article?id=1001&name=jack')}>searchParam传参</button>
            <button onClick={() => navigate('/article/1001')}>params传参</button>
        </div>
    )
}

export default Login;
