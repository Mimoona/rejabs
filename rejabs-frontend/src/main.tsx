import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import {BrowserRouter} from "react-router-dom";
import {AuthProvider} from "./context/AuthContext.tsx"
import {BoardProvider} from "./context/BoardContext.tsx";
import {CardProvider} from "./context/CardContext.tsx";


createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <BoardProvider>
                    <CardProvider>
                        <App/>
                    </CardProvider>
                </BoardProvider>
            </AuthProvider>
        </BrowserRouter>
    </StrictMode>,
)
