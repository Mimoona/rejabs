import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import {BrowserRouter} from "react-router-dom";
import {AuthProvider} from "./context/AuthContext.tsx"
import {BoardProvider} from "./context/BoardContext.tsx";
import {CardProvider} from "./context/CardContext.tsx";
import {BoardListProvider} from "./context/BoardListContext.tsx";


createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <BoardProvider>
                    <BoardListProvider>
                        <CardProvider>
                            <App/>
                        </CardProvider>
                    </BoardListProvider>
                </BoardProvider>
            </AuthProvider>
        </BrowserRouter>
    </StrictMode>,
)
