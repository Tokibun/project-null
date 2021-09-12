import React from "react";
import {Box} from "../box/box";

export function Topbar(props: {
  bannerImageURL: string;
  description: string;
  displayName: string;
}): JSX.Element {
  return (
    <section className="hero is-centered">
      <div className="hero-body">
        <div className="container has-text-centered">
          <div
            style={{
              width: "100%",
              height: 200,
              backgroundImage: `url(${props.bannerImageURL})`,
              backgroundPosition: "center"
            }}
          ></div>
          <h1 className="title">{props.displayName}</h1>
          <Box>
            <h2 className="subtitle">{props.description}</h2>
          </Box>
        </div>
      </div>
    </section>
  );
}
