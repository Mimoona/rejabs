import { useCard } from "../hooks/useCard";
import { useMemo } from "react";
import type { Card } from "../types/Card";

export function useDashboardData() {
    const { cards } = useCard();

    return useMemo(() => {
        const now = new Date();
        const in3Days = new Date();
        in3Days.setDate(now.getDate() + 3);

        const last7Days = new Date();
        last7Days.setDate(now.getDate() - 7);

        const upcoming:Card[] = cards.filter(c => {
            const due = new Date(c.dueDate);
            return due >= now && due <= in3Days;
        });

        const overdue:Card[] = cards.filter(c => {
            const due = new Date(c.dueDate);
            return due < now && !c.labels.includes("DONE");
        });

        const recent = cards.filter(c => {
            const created = new Date(c.createdAt);
            const updated = new Date(c.updatedAt);
            return created >= last7Days || updated >= last7Days;
        });

        const labelCounts = cards.flatMap(c => c.labels)
            .reduce((acc, label) => {
                acc[label] = (acc[label] || 0) + 1;
                return acc;
            }, {} as Record<string, number>);

        return {
            upcoming,
            overdue,
            recent,
            labelCounts
        };
    }, [cards]);
}
