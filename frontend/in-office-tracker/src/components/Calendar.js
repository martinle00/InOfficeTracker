import { useState, useEffect } from 'react';
import StatsTable from './StatsTable';
import CalendarHeader from './CalendarHeader';
import CalendarCell from './CalendarCell';

const months = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE', 'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'];

function Calendar() {
  const [changed, setChanged] = useState(false);
  const [data, setData] = useState(null);
  const [cellColors, setCellColors] = useState(
  Array.from({ length: 5 }, () => Array(5).fill('grey'))
);

  const weekDays = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];

  const [calendarGrid, setCalendarGrid] = useState(Array.from({ length: 5 }, () => Array(5).fill(null)));

  const now = new Date();
  const [currentMonth, setCurrentMonth] = useState(now.getMonth());
  const [currentYear, setCurrentYear] = useState(now.getFullYear());
  const lowerCaseMonth = months[currentMonth].charAt(0) + months[currentMonth].slice(1).toLowerCase();

// After fetching data and getting OfficeDays
const buildCalendarGrid = (officeDays) => {
  const weekDays = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];
  const grid = Array.from({ length: 5 }, () => Array(5).fill(null));

  // Map dates to their weekday index
  const dateMap = {};
  officeDays.forEach(({ Date, Day }) => {
    dateMap[Date] = weekDays.indexOf(Day);
  });

  // Sort dates numerically
  const sortedDates = officeDays.map(d => d.Date).sort((a, b) => a - b);

  // Find the starting weekday index for the 1st
  const firstDayIdx = dateMap[sortedDates[0]];

  let row = 0, col = firstDayIdx;
  sortedDates.forEach(date => {
    grid[row][col] = { date, value: '' };
    col++;
    if (col > 4) {
      col = 0;
      row++;
    }
  });

  return grid;
};

const handleSave = () => {
  const userOfficeDays = [];
  const userAtHomeDays = [];
  const userAbsentDays = [];

  for (let i = 0; i < 5; i++) {
    for (let j = 0; j < 5; j++) {
      const cell = calendarGrid[i][j];
      const color = cellColors[i][j];
      if (cell?.date) {
        if (color === 'green') userOfficeDays.push(cell.date);
        else if (color === 'yellow') userAtHomeDays.push(cell.date);
        else if (color === 'grey') userAbsentDays.push(cell.date);
      }
    }
  }

  // You can now use these arrays as needed, for example:
  console.log('userOfficeDays:', userOfficeDays);
  console.log('userAtHomeDays:', userAtHomeDays);
  console.log('userAbsentDays:', userAbsentDays);

  fetch('http://52.62.144.239:8080/month/save', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    // Add other headers if needed
  },
  body: JSON.stringify({
    // Your data here, for example:
    updatedOfficeDays: userOfficeDays,
    updatedAtHomeDays: userAtHomeDays,
    updatedAbsentDays: userAbsentDays,
    month: months[currentMonth].toUpperCase()
  }),
})
  .then(response => response.json())
  .then(data => {
    console.log('Success:', data);
  })
  .catch(error => {
    console.error('Error:', error);
  });

  setChanged(false);
  // Add your save logic here (e.g., send these arrays to your backend)
};

useEffect(() => {
  const monthName = months[currentMonth].toUpperCase();
  fetch(`http://52.62.144.239:8080/calendar?month=${monthName}`)
    .then(response => response.json())
    .then(json => {
      setData(json);
      if (json.OfficeDays) {
        const grid = buildCalendarGrid(json.OfficeDays);

        // Prepare a color grid based on user*Days arrays
        const colorGrid = Array.from({ length: 5 }, () => Array(5).fill('grey'));
        for (let i = 0; i < 5; i++) {
          for (let j = 0; j < 5; j++) {
            const cell = grid[i][j];
            if (cell?.date) {
              if (json.userOfficeDays && json.userOfficeDays.includes(cell.date)) {
                colorGrid[i][j] = 'green';
              } else if (json.userAtHomeDays && json.userAtHomeDays.includes(cell.date)) {
                colorGrid[i][j] = 'yellow';
              } else if (json.userAbsentDays && json.userAbsentDays.includes(cell.date)) {
                colorGrid[i][j] = 'grey';
              } else {
                colorGrid[i][j] = 'grey'; // default
              }
            }
          }
        }

        setCalendarGrid(grid);
        setCellColors(colorGrid);
      }
    })
    .catch(error => console.error('Error fetching data:', error));
}, [currentMonth, currentYear]);

  // Count colors only for cells with a date
let totalGrey = 0, totalGreen = 0, totalYellow = 0, totalWorkingDays = 0;
for (let i = 0; i < 5; i++) {
  for (let j = 0; j < 5; j++) {
    if (calendarGrid[i][j]?.date) {
      if (cellColors[i][j] === 'grey') totalGrey++;
      if (cellColors[i][j] === 'green') totalGreen++;
      if (cellColors[i][j] === 'yellow') totalYellow++;
      if (cellColors[i][j] !== 'grey') totalWorkingDays++;
    }
  }
}

  const handlePrevMonth = () => {
    setCurrentMonth(prev => {
      if (prev === 0) {
        setCurrentYear(y => y - 1);
        return 11;
      }
      console.log('Left arrow pressed, current month:', prev);
      return prev - 1;
    });
  };

  const handleNextMonth = () => {
    setCurrentMonth(prev => {
      if (prev === 11) {
        setCurrentYear(y => y + 1);
        return 0;
      }
      console.log('Right arrow pressed, current month:', prev);
      return prev + 1;
    });
  };

const actualConnection = totalWorkingDays > 0 ? totalGreen / totalWorkingDays : 0;
const actualConnectionPercent = (actualConnection * 100).toFixed(0);
const connectionColor = actualConnection >= 0.5 ? 'highlight-green' : 'highlight-red';

  return (
    <div className="calendar-grid-container">
      <div>
        <CalendarHeader 
          month={lowerCaseMonth} 
          year={currentYear} 
          onPrev={handlePrevMonth}
          onNext={handleNextMonth}
        />
        <table className="calendar-table">
          <thead>
            <tr>
              {weekDays.map(day => <th key={day}>{day}</th>)}
            </tr>
          </thead>
<tbody>
  {calendarGrid.map((week, i) => (
    <tr key={i}>
      {week.map((cell, j) => {
        let value = '';
        if (cell?.date) {
          if (cellColors[i][j] === 'grey') value = 'Non-Working day';
          else if (cellColors[i][j] === 'yellow') value = 'WFH day';
          else if (cellColors[i][j] === 'green') value = 'In Office day';
        }
        return (
          <CalendarCell
            key={j}
            value={value}
            date={cell?.date || ''}
            color={cellColors[i][j]}
            onCellClick={() => {
              if (!cell?.date) return;
              setCellColors(prev => {
                const COLORS = ['grey', 'green', 'yellow'];
                const currentIdx = COLORS.indexOf(prev[i][j]);
                const nextColor = COLORS[(currentIdx + 1) % COLORS.length];
                const updated = prev.map(row => [...row]);
                updated[i][j] = nextColor;
                return updated;
              });
              setChanged(true);
            }}
          />
        );
      })}
    </tr>
  ))}
</tbody>
        </table>
      </div>
      <StatsTable
        totalNonWorking={totalGrey}
        totalOffice={totalGreen}
        totalWFH={totalYellow}
        inOfficeDays={data ? data.RequiredInOfficeDays : 0}
        actualConnectionPercent={actualConnectionPercent}
        connectionColor={connectionColor}
        totalWorkingDays={totalWorkingDays}
/>
      <button
        className={`save-button${changed ? ' changed' : ''}`}
        onClick={handleSave}
        disabled={!changed}
      >
        Save
      </button>
    </div>
  );
}

export default Calendar;