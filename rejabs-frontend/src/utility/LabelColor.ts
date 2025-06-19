export const getColorFromLabel = (label: string) => {
    const labelColors: Record<string, string> = {
        URGENT: "red",
        DESIGN: "purple",
        BACKEND: "green",
        FRONTEND: "orange",
        QA: "blue",
        TODO: "#4DB8FF",
        IN_PROGRESS: "darkorange",
        DONE: "limegreen",
        HIGH_PRIORITY: "orangered",
        MEDIUM_PRIORITY: "coral",
        LOW_PRIORITY: "#23b07f",
        BUG: "red",
        FEATURE: "#d243d2",
        PLAN: "#328cd9"

    };
    return labelColors[label] || "gray";
};