import {createContext, useEffect, useState} from "react";
import type {Card, CardContextType} from "../types/Card.ts";

import api from "../api/axios.ts";

export const CardContext = createContext<CardContextType | undefined>(undefined);
export const CardProvider = ({children}: { children: React.ReactNode }) => {

    const [cards, setCards] = useState<Card[]>([]);
    const [error, setError] = useState<string | null>(null);

    const fetchCards = async () => {
        try {
            const response = await api.get<Card[]>(`/cards`);
            if (response.status === 200) {
                console.log(response.data);
                setCards(response.data);
                setError(null);
            }
        } catch (e: any) {
            console.error("Failed to fetch Cards", e);
            setCards([]);
            setError(e.message ?? "Unknown error");
        }
    };

    const refreshCards = async () => {
        await fetchCards();
    };

    useEffect(() => {
        fetchCards();
    }, []);

    const getCard = async (id: string): Promise<Card> => {
        const res = await api.get(`/cards/${id}`);
        return res.data;
    };

    const createCard = async (data: Partial<Card>): Promise<Card | null> => {
        try {
            const res = await api.post('/cards/create', data);
            setCards((prev: Card[]) => [...prev, res.data]);
            return res.data;
        } catch (e: any) {
            console.error("Failed to create a card", e);
            setError(e.message ?? "Unknown error");
            return null;
        }
    };

    const updateCard = async (cardId: string, data: Partial<Card>): Promise<Card | null> => {
        try {
            const res = await api.put(`/cards/${cardId}`, data);
            if(res){
                setCards((prevCards: Card[]) =>
                    prevCards.map(card => (card.cardId === cardId ? res.data : card))
                );
            }
            return res.data;
        } catch (e: any) {
            console.error("Failed to update card", e);
            setError(e.message ?? "Unknown error");
            return null;
        }
    };

    const deleteCard = async (cardId: string): Promise<boolean> => {
        try {
            await api.delete(`/cards/${cardId}`);
            setCards((prevCards: Card[]) => prevCards.filter(card => card.cardId !== cardId));
            return true;
        } catch (e: any) {
            console.error("Failed to delete card", e);
            setError(e.message ?? "Unknown error");
            return false;
        }
    };

    return (
        <CardContext.Provider value={{
            cards,
            setCards,
            getCard,
            createCard,
            updateCard,
            deleteCard,
            fetchCards,
            refreshCards,
            error,
            setError
        }}>
            {children}
        </CardContext.Provider>
    );
};