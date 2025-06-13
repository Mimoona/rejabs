import {createContext, useEffect, useState} from "react";
import api from "../api/axios.ts";
import type {BoardList, BoardListContextType} from "../types/BoardList.ts";

export const BoardListContext = createContext<BoardListContextType | undefined>(undefined);
export const BoardListProvider = ({children}: { children: React.ReactNode }) => {

    const [boardLists, setBoardLists] = useState<BoardList[]>([]);
    const [error, setError] = useState<string | null>(null);

    const fetchLists = async () => {
        try {
            const response = await api.get<BoardList[]>(`/board-list`);
            if (response.status === 200) {
                setBoardLists(response.data);
                console.log(response.data);
                setError(null);
            }
        } catch (e: any) {
            console.error("Failed to fetch Board Lists", e);
            setBoardLists([]);
            setError(e.message ?? "Unknown error");
        }
    };

    const refreshBoardLists = async () => {
        await fetchLists();
    };

    useEffect(() => {
        fetchLists();
    }, []);

    const getBoardList = async (id: string): Promise<BoardList> => {
        const res = await api.get(`/board-list/${id}`);
        return res.data;
    };

    const createBoardList = async (data: Partial<BoardList>): Promise<BoardList | null> => {
        try {
            const res = await api.post('/board-list/create', data);
            setBoardLists((prev: BoardList[]) => [...prev, res.data]);
            return res.data;
        } catch (e: any) {
            console.error("Failed to create a board list", e);
            setError(e.message ?? "Unknown error");
            return null;
        }
    };

    const updateBoardList = async (listId: string, data: Partial<BoardList>): Promise<BoardList | null> => {
        try {
            const res = await api.put(`/board-list/${listId}`, data);
            setBoardLists((prevBoardLists: BoardList[]) =>
                prevBoardLists.map(boardList => (boardList.boardListId === listId ? res.data : boardList))
            );
            return res.data;
        } catch (e: any) {
            console.error("Failed to update board list", e);
            setError(e.message ?? "Unknown error");
            return null;
        }
    };

    const deleteBoardList = async (listId: string): Promise<boolean> => {
        try {
            await api.delete(`/board-list/${listId}`);
            setBoardLists((prevBoardLists: BoardList[]) => prevBoardLists.filter(boardList => boardList.boardListId !== listId));
            return true;
        } catch (e: any) {
            console.error("Failed to delete board list", e);
            setError(e.message ?? "Unknown error");
            return false;
        }
    };

    return (
        <BoardListContext.Provider value={{
            boardLists,
            setBoardLists,
            getBoardList,
            createBoardList,
            updateBoardList,
            deleteBoardList,
            fetchLists,
            refreshBoardLists,
            error
        }}>
            {children}
        </BoardListContext.Provider>
    );
};
