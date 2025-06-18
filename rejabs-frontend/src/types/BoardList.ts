export type BoardList = {
    boardListId: string,
    listTitle: string,
    boardId: string,
    position: number,
}


export type BoardListContextType = {
    boardLists: BoardList[];
    getBoardList: (id: string) => Promise<BoardList>;
    createBoardList: (data: Partial<BoardList>) => Promise<BoardList>;
    updateBoardList: (id: string, data: Partial<BoardList>) => Promise<BoardList | null>;
    deleteBoardList: (id: string) => Promise<boolean>;
    fetchLists: () => Promise<void>;
    refreshBoardLists: () => Promise<void>;
}