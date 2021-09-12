import React from "react";

const dimension = "60";

export const SidebarIcon = (props: { svg: string; onClick?: () => void }) => {
  return (
    <div
      onClick={props.onClick}
      style={{ cursor: `${props.onClick ? "pointer" : ""}` }}
    >
      <svg width={dimension} height={dimension}>
        <image href={props.svg} width={dimension} height={dimension} />
      </svg>
    </div>
  );
};
