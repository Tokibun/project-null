import React from "react";
import "./index.scss";
import { Sidebar } from "./components/sidebar/sidebar";
import styled from "styled-components";
import { MainContentArea } from "./components/mainContentArea/mainContentArea";
import { LoginPage } from "./components/loginPage/loginPage";

const MainPageWrapper = styled.div`
  margin: auto;
  max-width: 1380px;
`;

const App: React.FC = () => {
  return (
    <MainPageWrapper>
      <div className="columns">
        <div className="column is-narrow">
          <Sidebar />
        </div>
        <div className="column">
          <MainContentArea content={<LoginPage />} />
        </div>
      </div>
    </MainPageWrapper>
  );
};

export default App;
