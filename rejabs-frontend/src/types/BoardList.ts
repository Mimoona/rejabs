import type {Board} from "./Board.ts";

export type BoardList = {
    boardListId: string,
    listTitle: string,
    boardId: string,
    position: number,
}


export type BoardListContextType = {
    boardLists: BoardList[];
    getBoardList: (id: string) => Promise<Board>;
    createBoardList: (data: Partial<Board>) => Promise<Board | null>;
    updateBoardList: (id: string, data: Partial<Board>) => Promise<Board | null>;
    deleteBoardList: (id: string) => Promise<boolean>;
    fetchLists: () => Promise<void>;
    refreshBoardLists: () => Promise<void>;
}