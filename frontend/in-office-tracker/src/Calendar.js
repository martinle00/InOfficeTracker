import React from "react";

const daysOfWeek = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
const juneDays = [
  [ "",   "",   1,   2,   3,   4 ],
  [ 5,    6,   7,   8,   9,  10 ],
  [ 12,  13,  14,  15,  16,  17 ],
  [ 19,  20,  21,  22,  23,  24 ],
  [ 26,  27,  28,  29,  30,  "" ],
];

export default function Calendar() {
  return (
    <div style={{
      minHeight: "100vh",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      background: "#f0f0f0",
      padding: 16,
    }}>
      <div
        style={{
          width: "100%",
          maxWidth: 600,
          border: "3px solid black",
          borderRadius: 12,
          padding: 16,
          background: "#fff",
          fontFamily: "sans-serif",
          boxShadow: "0 4px 24px rgba(0,0,0,0.08)",
        }}
      >
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: 12,
          }}
        >
          <span style={{ fontWeight: "bold", fontSize: 18, cursor: "pointer" }}>&lt; May</span>
          <span style={{ fontSize: 32, fontWeight: "bold" }}>June</span>
          <span style={{ fontWeight: "bold", fontSize: 18, cursor: "pointer" }}>July &gt;</span>
        </div>
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(6, 1fr)",
            borderBottom: "3px solid black",
            marginBottom: 6,
            fontSize: 16,
          }}
        >
          {daysOfWeek.map((day) => (
            <div key={day} style={{ textAlign: "center", fontWeight: "bold" }}>
              {day}
            </div>
          ))}
        </div>
        <div
          style={{
            display: "grid",
            gridTemplateRows: "repeat(5, minmax(40px, 1fr))",
            gridTemplateColumns: "repeat(6, 1fr)",
            gap: 0,
          }}
        >
          {juneDays.flat().map((date, idx) => (
            <div
              key={idx}
              style={{
                border: "1.5px solid black",
                minHeight: 40,
                height: "8vw",
                maxHeight: 70,
                textAlign: "center",
                verticalAlign: "middle",
                fontSize: 20,
                paddingTop: 10,
                background: date ? "#fff" : "#f9f9f9",
                boxSizing: "border-box",
              }}
            >
              {date}
            </div>
          ))}
        </div>
      </div>
      <style>{`
        @media (max-width: 600px) {
          .calendar-container {
            padding: 4px !important;
          }
          .calendar-header {
            font-size: 20px !important;
          }
          .calendar-day {
            font-size: 12px !important;
          }
          .calendar-cell {
            font-size: 14px !important;
            min-height: 32px !important;
            padding-top: 4px !important;
          }
        }
      `}</style>
    </div>
  );
}