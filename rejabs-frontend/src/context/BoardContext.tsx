import type {Board, BoardContextType} from "../types/Board.ts";
import {createContext, useEffect, useState} from "react";
import api from "../api/axios.ts";


export const BoardContext = createContext<BoardContextType | undefined>(undefined);

export const BoardProvider = ({children}: { children: React.ReactNode }) => {

    const [boards, setBoards] = useState<Board[]>([]);
    const [error, setError] = useState<string | null>(null);


    const fetchBoards = async () => {
        try {
            const response = await api.get<Board[]>('/boards');
            if (response.status === 200) {
                setBoards(response.data);
                setError(null);
            }
        } catch (e: any) {
            console.error("Failed to fetch Boards", e);
            setBoards([]);
            setError(e.message ?? "Unknown error");
        }
    };

    // Non-async wrapper function
    const refreshBoards = async () => {
        await fetchBoards();
    };

    useEffect(() => {
        fetchBoards();
    }, []);

    const getBoard = async (id: string): Promise<Board> => {
        const res = await api.get(`/boards/${id}`);
        return res.data;
    };

    const createBoard = async (data: Partial<Board>): Promise<Board | null> => {
        try {
            const res = await api.post('/boards', data);
            setBoards((prev: Board[]) => [...prev, res.data]);
            return res.data;
        } catch (e: any) {
            console.error("Create board failed", e);
            setError(e.message ?? "Unknown error");
            return null;
        }
    };

    const updateBoard = async (id: string, data: Partial<Board>): Promise<Board | null> => {
        try {
            const res = await api.put(`/boards/${id}`, data);
            setBoards((prevBoards: Board[]) =>
                prevBoards.map(board => (board.boardId === id ? res.data : board))
            );
            return res.data;
        } catch (e: unknown) {
            console.error("Update board failed", e);
            setError(e.message ?? "Unknown error");
            return null;
        }
    };

    const deleteBoard = async (id: string): Promise<boolean> => {
        try {
            await api.delete(`/boards/${id}`);
            setBoards((prevBoards: Board[]) => prevBoards.filter(board => board.boardId !== id));
            return true;
        } catch (e: unknown) {
            console.error("Delete board failed", e);
            setError(e.message ?? "Unknown error");
            return false;
        }
    };

    return (
        <BoardContext.Provider
            value={{boards, getBoard, createBoard, updateBoard, deleteBoard, refreshBoards, error, setError}}>
            {children}
        </BoardContext.Provider>
    );
};
