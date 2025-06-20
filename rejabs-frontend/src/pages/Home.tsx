import type {Card} from "../types/Card.ts";
import {useDashboardData} from "../hooks/useDashboardData.tsx";
import {JSX} from "react";
import { CalendarDaysIcon, ClockIcon, PencilSquareIcon, TagIcon } from "@heroicons/react/24/solid";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";


const LABEL_COLORS: Record<string, string> = {
    DESIGN: "#d946ef",
    BACKEND: "#10b981",
    FRONTEND: "#f97316",
    BLOCKED: "#f87171",
    REVIEW: "#6366f1",
    QA: "#14b8a6",
    TODO: "#facc15",
    IN_PROGRESS: "#fb923c",
    DONE: "#22c55e",
    HIGH_PRIORITY: "#e11d48",
    MEDIUM_PRIORITY: "#fbbf24",
    LOW_PRIORITY: "#4ade80",
    BUG: "#dc2626",
    FEATURE: "#10b981",
    PLAN: "#fde68a",
};

const Home = () => {
    const { upcoming, overdue, recent, labelCounts } = useDashboardData();

    const pieData = Object.entries(labelCounts).map(([label, count]) => ({
        name: label.replace("_", " "),
        value: count,
        color: label[label] || "#94a3b8",
    }));

    return (
        <div className="flex flex-col min-h-screen bg-slate-50 p-6 items-center">
            <h1 className="text-3xl font-bold mb-8 text-slate-800"> Your Task Dashboard</h1>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-6xl">
                <DashboardCard title="Upcoming Tasks" icon={<CalendarDaysIcon className="h-6 w-6 text-blue-500"/>}
                               count={upcoming.length}>
                    <TasksSummary cards={upcoming}/>
                </DashboardCard>

                <DashboardCard title=" Overdue Tasks" icon={<ClockIcon className="h-6 w-6 text-red-500"/>}
                               count={overdue.length}>
                    <TasksSummary cards={overdue}/>
                </DashboardCard>

                <DashboardCard title=" Recent Activity" icon={<PencilSquareIcon className="h-6 w-6 text-green-500"/>}
                               count={recent.length}>
                    <TasksSummary cards={recent}/>
                </DashboardCard>

                <DashboardCard title=" Label Summary" icon={<TagIcon className="h-6 w-6 text-yellow-500"/>}
                               count={pieData.length}>
                    {pieData.length > 0 ? (
                        <ResponsiveContainer width="100%" height={200}>
                            <PieChart>
                                <Pie data={pieData} dataKey="value" nameKey="name" outerRadius={80}>
                                    {pieData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={entry.color}/>
                                    ))}
                                </Pie>
                                <Tooltip/>
                            </PieChart>
                        </ResponsiveContainer>
                    ) : (
                        <p className="text-gray-400 text-sm">No label data available.</p>
                    )}
                </DashboardCard>
            </div>
        </div>
    )
};


function DashboardCard({
    title,
    icon,
    count,
    children,
}: {
    title: string;
    icon: React.ReactNode;
    count: number;
    children: React.ReactNode | JSX.Element;
}) {
    return (
        <div className="bg-gray-200 rounded-xl shadow-lg p-4 flex flex-col">
            <div className="flex items-center justify-between mb-3">
                <div className="flex items-center gap-2 text-lg font-semibold text-gray-800">
                    {icon}
                    <span>{title}</span>
                </div>
                <span className="bg-blue-100 text-blue-700 text-xs font-bold px-2 py-1 rounded-full">{count}</span>
            </div>
            <div className="text-sm text-gray-600 flex-1 overflow-auto">{children}</div>
        </div>
    );
}

function TasksSummary({ cards }: { cards: Card[] }) {
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
                                    style={{ backgroundColor: LABEL_COLORS[label] || "gray" }}
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
export default Home;