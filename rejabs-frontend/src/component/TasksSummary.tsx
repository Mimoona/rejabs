import type {Card} from "../types/Card.ts";
import {getColorFromLabel} from "../utility/LabelColor.ts";

const TasksSummary = ({cards}: { cards: Card[] }) => {
    if (!cards.length) return <p className="text-gray-400">No cards found.</p>;

    return (
        <ul className="space-y-2">
            {cards.map((card) => (
                <li key={card.cardId} className="border border-gray-200 rounded p-2 hover:bg-gray-50">
                    <p className="font-medium text-gray-800 truncate">{card.cardTitle}</p>
                    {card.dueDate && (
                        <p className="text-xs text-gray-500">Due: {new Date(card.dueDate).toLocaleDateString()}</p>
                    )}
                    {card.labels.length > 0 && (
                        <div className="flex flex-wrap gap-1 mt-1">
                            {card.labels.map((label) => (
                                <span
                                    key={label}
                                    className="text-white text-xs px-2 py-0.5 rounded-full"
                                    style={{backgroundColor: getColorFromLabel(label) || "gray"}}
                                >
                  {label.replace("_", " ")}
                </span>
                            ))}
                        </div>
                    )}
                </li>
            ))}
        </ul>
    );
}

export default TasksSummary;