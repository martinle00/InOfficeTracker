function CalendarCell({ value, date, color, onCellClick }) {
  const cellClass = date ? `calendar-cell ${color}` : 'calendar-cell empty';

  return (
    <td
      className={cellClass}
      onClick={onCellClick}
      style={{
        position: 'relative',
        cursor: date ? 'pointer' : 'default',
        background: !date ? '#fff' : undefined
      }}
    >
      {date && (
        <span
          style={{
            position: 'absolute',
            top: 2,
            right: 6,
            fontSize: '0.8em',
            color: '#888',
          }}
        >
          {date}
        </span>
      )}
      {value}
    </td>
  );
}
export default CalendarCell;