import {CalendarDaysIcon, ClockIcon, PencilSquareIcon, TagIcon} from "@heroicons/react/24/solid";
import {useDashboardData} from "../hooks/useDashboardData.tsx";
import {PieChart, Pie, Cell, Tooltip, ResponsiveContainer} from "recharts";
import {getColorFromLabel} from "../utility/LabelColor.ts";
import TasksSummary from "../component/TasksSummary.tsx";
import type {JSX} from "react";

const Home = () => {
    const {upcoming, overdue, recent, labelCounts} = useDashboardData();

    const pieData = Object.entries(labelCounts).map(([label, count]) => ({
        name: label.replace("_", " "),
        value: count,
        color: getColorFromLabel(label),
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

export default Home;