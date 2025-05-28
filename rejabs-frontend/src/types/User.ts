export type User = {
    githubId: string,
    username: string,
    email: string | null,
    avatarUrl: string
}

export interface AuthContextType {
    user: User | undefined | null;
    setUser: React.Dispatch<React.SetStateAction<User | undefined | null>>;
    login: () => void
    logout: () => void
}