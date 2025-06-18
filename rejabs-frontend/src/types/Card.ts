
export type Card = {
    cardId: string,
    cardTitle: string,
    description: string,
    listId: string,
    position: number,
    labels: string[],
    dueDate: Date
}

export type CardContextType = {
    cards: Card[],
    getCard: (id: string) => Promise<Card>,
    createCard: (data: Partial<Card>) => Promise<Card | null>,
    updateCard: (id: string, data: Partial<Card>) => Promise<Card | null>,
    deleteCard: (id: string) => Promise<boolean>,
    refreshCards: () => Promise<void>
}
