import {useContext} from "react";
import {CardContext} from "../context/CardContext.tsx";

export const useCard = () => {
    const context = useContext(CardContext);
    if (!context) throw new Error('useCard must be used within a CardProvider');
    return context;
};