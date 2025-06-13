import {useContext} from "react";
import {BoardListContext} from "../context/BoardListContext.tsx";

export const useBoardList = () => {
    const context = useContext(BoardListContext);
    if (!context) throw new Error('useBoardList must be used within a BoardListProvider');
    return context;
};