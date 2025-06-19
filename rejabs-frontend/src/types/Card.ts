
export type Card = {
    cardId: string,
    cardTitle: string,
    description: string,
    listId: string,
    position: number,
    labels: string[],
    dueDate: string,
    createdAt: Date,
    updatedAt: Date
}

export type CardContextType = {
    cards: Card[],
    getCard: (id: string) => Promise<Card>,
    createCard: (data: Partial<Card>) => Promise<Card>,
    updateCard: (id: string, data: Partial<Card>) => Promise<Card | null>,
    deleteCard: (id: string) => Promise<boolean>,
    refreshCards: () => Promise<void>
}
