import React from "react";

export function Cell(props: {
  label: string;
  value: string;
  className?: string;
  state: any;
  onChange: (value: string) => (_: any) => void;
}): JSX.Element {
  return (
    <div className="column is-12">
      <div className="columns">
        <div className="column is-marginless">
          <small>
            <b>{props.label}</b>
          </small>
        </div>
        <div className="column is-half is-marginless">
          <input
            className="input"
            disabled={props.className === "disabled"}
            type={props.className || "text"}
            onChange={props.onChange(props.value)}
            value={`${props.state[props.value] || ""}`}
          ></input>
        </div>
      </div>
    </div>
  );
}
