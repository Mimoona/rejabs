
export type Board = {
    boardId: string,
    title: string,
    collaborators: Collaborator[],
}

export type Collaborator = {
    collaboratorName: string;
    collaboratorEmail: string;
    collaboratorAvatar: string;
}

export type BoardContextType = {
    boards: Board[];
    getBoard: (id: string) => Promise<Board>;
    createBoard: (data: Partial<Board>) => Promise<Board | null>;
    updateBoard: (id: string, data: Partial<Board>) => Promise<Board | null>;
    deleteBoard: (id: string) => Promise<boolean>;
    refreshBoards: () => Promise<void>;
    error: string;
    setError: (error:string) => void
}