import './App.css'
import {Route, Routes} from "react-router-dom";
import Layout from "./component/Layout.tsx";
import ProtectedRoute from "./component/ProtectedRoute.tsx";
import LandingPage from "./pages/LandingPage.tsx";
import Profile from "./pages/Profile.tsx";
import Boards from "./pages/Boards.tsx";
import Home from "./pages/Home.tsx";
import SignUp from "./pages/SignUp.tsx";
import SignIn from "./pages/SignIn.tsx";
import NotFound from "./pages/NotFound.tsx";
import OAuthCallback from "./component/OAuthCallback.tsx";

function App() {

    return (
        <Routes>
            <Route path="*" element={<NotFound/>}/>
            <Route index element={<LandingPage/>}/>
            <Route path="/signup" element={<SignUp/>}/>
            <Route path="/signin" element={<SignIn/>}/>
            <Route path="/oauth2/callback" element={<OAuthCallback/>}/>
            <Route path="/" element={<Layout/>}>
                <Route element={<ProtectedRoute/>}>
                    <Route path="profile" element={<Profile/>}/>
                    <Route path="home" element={<Home/>}/>
                    <Route path="boards/:boardId" element={<Boards/>}/>
                </Route>
            </Route>
        </Routes>
    );
}

export default App
