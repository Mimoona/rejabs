export type User = {
    id: number,
    login: string,
    email: string | null,
    avatar_url: string
}

export interface AuthContextType {
    user: User | undefined | null;
    setUser: React.Dispatch<React.SetStateAction<User | undefined | null>>;
    login: () => void
    logout: () => void
}