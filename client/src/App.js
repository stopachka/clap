import React, { useEffect, useState } from "react";
import "./App.css";

const API_ROOT =
  process.env.NODE_ENV === "development" ? "http://localhost:4000" : "";

function App() {
  const [res, setRes] = useState(null);
  useEffect(() => {
    fetch(`${API_ROOT}/api/ping`)
      .then((x) => x.json())
      .then(setRes, () => setRes("uh noz"));
  }, []);
  return (
    <div className="App">
      <header>
        <h1>
          <span role="img" aria-label="rocket">
            🚀
          </span>{" "}
          Let's hack
        </h1>
        <h4>{res ? <pre>{JSON.stringify(res, null, 2)}</pre> : "Loading.."}</h4>
      </header>
    </div>
  );
}

export default App;
