export type User = {
    id: string,
    username: string,
    email: string,
    password: string | null,
    avatarUrl: string,
}

export interface AuthContextType {
    user: User | undefined | null
    setUser: React.Dispatch<React.SetStateAction<User | undefined | null>>;
    loginOauthUser: () => void;
    registerLocalUser: (username: string, email: string, password: string) => Promise<void>;
    loginLocalUser: (email: string, password: string) => Promise<void>;
    logout: () => Promise<void>;
    isLoading: boolean
}