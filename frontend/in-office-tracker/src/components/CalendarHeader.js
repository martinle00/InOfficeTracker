import React from 'react';

function CalendarHeader({ month = "July", year = 2025, onPrev, onNext }) {
  return (
    <div className="calendar-header">
      <button className="calendar-arrow" onClick={onPrev} aria-label="Previous Month">
        &#8592;
      </button>
      <span className="calendar-header-title">
        {month} {year}
      </span>
      <button className="calendar-arrow" onClick={onNext} aria-label="Next Month">
        &#8594;
      </button>
    </div>
  );
}

export default CalendarHeader;