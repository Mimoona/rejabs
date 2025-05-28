import './App.css'
import {Route, Routes} from "react-router-dom";
import Layout from "./component/Layout.tsx";
import ProtectedRoute from "./component/ProtectedRoute.tsx";
import Home from "./pages/Home.tsx";
import Login from "./pages/Login.tsx";
import Profile from "./pages/Profile.tsx";
import Boards from "./pages/Boards.tsx";

function App() {

    return (
        <Routes>
            <Route index element={<Home/>}/>
            <Route path="signup" element={<Login/>}/>
            <Route path="/" element={<Layout/>}>
                <Route element={<ProtectedRoute/>}>
                    <Route path="profile" element={<Profile/>}/>
                    <Route path="boards" element={<Boards/>}/>
                </Route>
            </Route>
        </Routes>

    );
}

export default App
